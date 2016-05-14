package cn.edu.shnu.fb.interfaces.facade;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.application.LogService;
import cn.edu.shnu.fb.application.SalaryService;
import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.major.MajorType;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.interfaces.assembler.PlanAssembler;
import cn.edu.shnu.fb.interfaces.dto.FrontEndSalaryDTO;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.RejectedCourseInspectionDTO;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/8.
 */
@RequestMapping("/api")
@RestController
public class SalaryFacade {
    @Autowired
    SalaryService salaryService;
    @Autowired
    TermRepository termRepository;
    @Autowired
    LogService logService;
    @Autowired
    TeacherDao teacherDao;

    @ResponseBody
    @RequestMapping(value="/t/{teacherId}/term/{termYear}/{termPart}/s",method= RequestMethod.GET)
    public List<SalaryDTO> getTeacherSalaries(@PathVariable Integer teacherId , @PathVariable Integer termYear , @PathVariable Integer termPart){
        Term term = termRepository.findTermByYearAndPart(termYear , termPart);
        return salaryService.buildDTOSForTeacher(teacherId , term.getId(),false);
    }

    @ResponseBody
    @RequestMapping(value="/s/term/{termYear}/{termPart}/s",method= RequestMethod.GET)
    public List<SalaryDTO> exportExcel(@PathVariable Integer termYear , @PathVariable Integer termPart){
        Term term = termRepository.findTermByYearAndPart(termYear , termPart);
        return salaryService.buildAllDTOS(term.getId());
    }

    @ResponseBody
    @RequestMapping(value="/s/add",method= RequestMethod.POST)
    public void demandNewCourse(@RequestBody SalaryDTO salaryDTO){
        Teacher teacher = teacherDao.findOne(salaryDTO.getTeacherId());
        if(teacher!=null) {
            salaryDTO.setTeacherEntity(teacher);
            List<SalaryDTO> salaryDTOs = new ArrayList<>();
            salaryDTOs.add(salaryDTO);
            salaryService.persistSalaryDTOs(salaryDTOs);
        }
        logService.action("文修副修","申报");

    }


    @ResponseBody
    @RequestMapping(value="/s/update",method= RequestMethod.POST, consumes = "application/json")
    public void saveTeacherSalaries(@RequestBody FrontEndSalaryDTO fesDTO){
        if(fesDTO.getSalaryDTOs().size()>0) {
            salaryService.saveSalariesFromDTOS(fesDTO.getSalaryDTOs(), fesDTO.getSalaryAdjustments());
            logService.action("工作量", "更新");
        }
    }

    @ResponseBody
    @RequestMapping(value="/i/{impId}/t/{teacherId}/s/{salaryId}/reject",method= RequestMethod.POST, consumes = "application/json")
    public void rejectTeacherSalaries(@PathVariable Integer impId , @PathVariable Integer teacherId, @PathVariable Integer salaryId,@RequestBody String comment){
        if(impId!=0) {
            salaryService.rejectSalary(impId, teacherId, comment);
        }else{
            salaryService.deleteSalary(salaryId);
        }
        logService.action("课程","拒绝");
    }


    @ResponseBody
    @RequestMapping(value="/i/{impId}/t/{teacherId}/s/{salaryId}/reject/cancel",method= RequestMethod.POST, consumes = "application/json")
    public void cancelrejectTeacherSalaries(@PathVariable Integer impId , @PathVariable Integer teacherId, @PathVariable Integer salaryId,@RequestBody String comment){
        if(impId!=0) {
            salaryService.cancelSalary(impId, teacherId, comment);
        }else{
            salaryService.deleteSalary(salaryId);
        }
        logService.action("课程","取消拒绝课程");
    }

    @ResponseBody
    @RequestMapping(value="/i/term/{termYear}/{termPart}/resId/{resId}/reject",method= RequestMethod.GET)
    public List<RejectedCourseInspectionDTO> getRejectedSalaries(@PathVariable Integer termYear , @PathVariable Integer resId,@PathVariable Integer termPart){
        Term term = termRepository.findTermByYearAndPart(termYear , termPart);
        Teacher teacher = teacherDao.findOne(resId);
        List<MajorType> mt =  teacher.getMajorTypes();
        return salaryService.buildRejectedDTOs(term.getId(),mt);
    }

    @ResponseBody
    @RequestMapping(value="/s/{salaryId}/delete",method= RequestMethod.GET)
    public void getRejectedSalaries(@PathVariable Integer salaryId){
        salaryService.deleteSalary(salaryId);
    }
}
