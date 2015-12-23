package cn.edu.shnu.fb.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.shnu.fb.interfaces.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.Imp.SalaryRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.mergedClass.MergedClass;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;

/**
 * Created by bytenoob on 15/12/7.
 */
@Service
public class SalaryService {
    @Autowired
    ImpRepository impRepository;

    @Autowired
    TermRepository termRepository;
    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    TeacherDao teacherDao;

    public void deleteSalary(Integer salaryId){
        Salary salary = salaryRepository.findById(salaryId);
        List<Imp> imps = impRepository.findBySalary(salary);
        if(imps.size()>0){
            for(Imp imp : imps){
                imp.setSalary(null);
                impRepository.save(imp);
            }
        }
        salaryRepository.deleteById(salaryId);
    }
    public List<RejectedCourseInspectionDTO> buildRejectedDTOs(Integer termId){
        List<RejectedCourseInspectionDTO> RCIs = new ArrayList<>();
        Term term = termRepository.findTermById(termId);
        List<Imp> imps = impRepository.getRejectedImps(term);
        for(Imp imp : imps){
            GridEntityDTO geDTO = new GridEntityDTO(imp);
            Salary s = imp.getSalary();
            SalaryDTO sDTO= new SalaryDTO(s,imp.getTeachers().get(0),imp.getId());
            RCIs.add(new RejectedCourseInspectionDTO(sDTO,geDTO));
        }
        return RCIs;
    }
    public List<SalaryDTO> buildDTOSForTeacher(Integer teacherId ,Integer termId){
        Teacher teacher = teacherDao.findOne(teacherId);
        List<SalaryDTO> salaryDTOs = new ArrayList<>();
        SalaryDTO adjustDTO = new SalaryDTO(teacher);

        if(teacher!=null) {
            List<Imp> imps = impRepository.getImpByTeacherIdAndTermId(teacherId, termId);
            Map<String, List<Imp>> groupedImp = groupByCategoryType(imps);
            for (Map.Entry<String, List<Imp>> entry : groupedImp.entrySet()) {
                List<Imp> impGrouped = entry.getValue();

                if (impGrouped.size() > 0) {
                    if(impGrouped.get(0).getSalary() == null) {
                        salaryDTOs.add(new SalaryDTO(impGrouped, "本科", teacher, 0));
                    }else{
                        //Override
                        salaryDTOs.add(new SalaryDTO(impGrouped.get(0).getSalary(),teacher,impGrouped.get(0).getId()));
                    }
                }
            }
        }
        //Override
        Salary deduction = salaryRepository.findDedection(teacherId);
        if(deduction!=null){
            adjustDTO.setId(deduction.getId());
            adjustDTO.setBasicSalaryDeduction(deduction.getBasicSalaryDeduction());
            adjustDTO.setTermSalary(deduction.getBasicSalaryDeduction());
        }
        salaryDTOs.add(adjustDTO);
        SalaryDTO sumDTO = new SalaryDTO(salaryDTOs);
        salaryDTOs.add(sumDTO);
        return salaryDTOs;
    }
    public void rejectSalary(int impId , int teacherId , String comment){
        Imp imp = impRepository.getImpById(impId);
        Salary s = imp.getSalary();
        if(s!=null){
            s = salaryRepository.rejectSalary(s,comment);
            List<Imp> imps = impRepository.findBySalary(s);
            for(Imp i : imps){
                i.setSalary(s);
                impRepository.save(i);
            }
        }else{
            MergedClass mc = imp.getMergedClass();
            List<Imp> imps = new ArrayList<>();
            if(mc!=null) {
                imps = impRepository.findByMergedClass(mc);
            }else{
                imps.add(imp);
            }
            SalaryDTO sDTO = new SalaryDTO(imps, "本科", teacherDao.findOne(teacherId), 0);
            sDTO.setRejected(1);
            sDTO.setComment(comment);
            s = salaryRepository.persistSalary(sDTO);
            for(Imp i : imps){
                i.setSalary(s);
                impRepository.save(i);
            }
        }

    }
    public void saveSalariesFromDTOS(List<SalaryDTO> salaryDTOs , List<SalaryDTO> salaryAdjustments){
        SalaryDTO deductionDTO = salaryAdjustments.get(1);
        deductionDTO.setTermSalary(deductionDTO.getBasicSalaryDeduction());
        salaryDTOs.add(deductionDTO);
        for(SalaryDTO s : salaryDTOs) {
            if(!s.getCourseType().contains("扣")) {
                s.setDepartmentType(salaryAdjustments.get(0).getDepartmentType());
                s.setSalaryPerHour(salaryAdjustments.get(0).getSalaryPerHour());
                s.setTermSalary(s.getMonthlySalary() * 5);
            }
            Salary salaryEntity = salaryRepository.persistSalary(s);
            if(s.getImpId().size()>0 && !s.getCourseType().contains("扣")) {
                for (Integer id : s.getImpId()) {
                    Imp imp = impRepository.getImpById(id);
                    if(imp!=null) {
                        imp.setSalary(salaryEntity);
                        impRepository.save(imp);
                    }
                }
            }
        }
    }
    public Map<String, List<Imp>> groupByCategoryType(List<Imp> list) {
        Map<String, List<Imp>> map = new TreeMap<String, List<Imp>>();
        for (Imp o : list) {
            List<Imp> group = null;
            String keyString ;
            if(o.getMergedClass()!=null) {
                keyString = "M" + Integer.toString(o.getMergedClass().getId());
            }else{
                keyString = Integer.toString(o.getId());
            }
            group = map.get(keyString);

            if (group == null) {
                group = new ArrayList();
                map.put(keyString, group);
            }
            group.add(o);
        }
        return map;
    }


    public List<SalaryDTO> buildAllDTOS(Integer termId) {
        Iterable<Teacher> teachers = teacherDao.findAll();
        List<SalaryDTO> salaryDTOs = new ArrayList<>();
        for(Teacher teacher : teachers){
            salaryDTOs.addAll(buildDTOSForTeacher(teacher.getId(), termId));
        }
        return salaryDTOs;
    }

    public SalaryExcelDTO getSalaryExcelDTO(Integer termId){

        List<SalaryDTO> salaryDTOs=buildAllDTOS(termId);
        Term term=termRepository.findTermById(termId);
        return new SalaryExcelDTO(salaryDTOs,new SalaryHeaderDTO(term.getYear(),term.getPart()));
    }
}
