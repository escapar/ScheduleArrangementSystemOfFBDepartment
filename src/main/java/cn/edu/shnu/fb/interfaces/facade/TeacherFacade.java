package cn.edu.shnu.fb.interfaces.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.interfaces.assembler.PlanAssembler;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/18.
 */
@RequestMapping("/api")
@RestController
public class TeacherFacade {
    @Autowired
    TeacherDao teacherDao;
    @ResponseBody
    @RequestMapping(value="/t",method= RequestMethod.POST)
    public List<Teacher> planSpecGridEntityByMajorAndTerm(@RequestBody String name){
        return teacherDao.findByNameLike("%"+name+"%");
    }
}
