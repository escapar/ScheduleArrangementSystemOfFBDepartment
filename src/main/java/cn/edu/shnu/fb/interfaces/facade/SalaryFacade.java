package cn.edu.shnu.fb.interfaces.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.application.SalaryService;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.interfaces.assembler.PlanAssembler;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/8.
 */
@RequestMapping("/api")
@RestController
public class SalaryFacade {
    @Autowired
    SalaryService salaryService;

    @ResponseBody
    @RequestMapping(value="/t/{teacherId}/s",method= RequestMethod.GET) // PlanCourse Grid
    public List<SalaryDTO> getTeacherSalaries(@PathVariable Integer teacherId){
        return salaryService.buildDTOSForTeacher(teacherId);
    }
}
