package cn.edu.shnu.fb.interfaces.facade;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
    public List<Teacher> getByName(@RequestBody String name){
        return teacherDao.findByNameLike(name+"%");
    }

    @RequestMapping(value="/t/all",method= RequestMethod.GET)
    public Iterable<Teacher> getAll(){
        return teacherDao.findByNameIsNotNullOrderByIdCode();
    }
}
