package cn.edu.shnu.fb.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/7.
 */
@Service
public class SalaryService {
    @Autowired
    ImpRepository impRepository;
    @Autowired
    TeacherDao teacherDao;
    public List<SalaryDTO> buildDTOSForTeacher(Integer teacherId){
        Teacher teacher = teacherDao.findOne(teacherId);
        List<SalaryDTO> salaryDTOs = new ArrayList<>();
        if(teacher!=null) {
            List<Imp> imps = teacher.getImps();
            Map<Integer, List<Imp>> groupedImp = impRepository.groupByCategoryType(imps);
            for (Map.Entry<Integer, List<Imp>> entry : groupedImp.entrySet()) {
                List<Imp> impGrouped = entry.getValue();
                if (impGrouped.size() > 1) {
                    salaryDTOs.add(new SalaryDTO(impGrouped, "本科", teacher, 0));
                }
            }
        }
        return salaryDTOs;
    }

    public List<SalaryDTO> buildAllDTOS() {
        Iterable<Teacher> teachers = teacherDao.findAll();
        List<SalaryDTO> salaryDTOs = new ArrayList<>();
        for(Teacher teacher : teachers){
            salaryDTOs.addAll(buildDTOSForTeacher(teacher.getId()));
        }
        return salaryDTOs;
    }
}
