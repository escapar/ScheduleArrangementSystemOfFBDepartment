package cn.edu.shnu.fb.interfaces.facade;

import java.security.Principal;
import java.util.ArrayList;
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
import cn.edu.shnu.fb.domain.user.User;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.infrastructure.persistence.UserDao;
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
    @Autowired
    UserDao userDao;
    @ResponseBody
    @RequestMapping(value="/t",method= RequestMethod.POST)
    public List<Teacher> getByName(@RequestBody String name){
        return teacherDao.findByNameLike("%"+name+"%");
    }

    @RequestMapping(value="/t/resp",method= RequestMethod.GET)
    public List<Teacher> getResponsable(){
        List<Teacher> res = new ArrayList<>();
        List<User> users = userDao.findByRole(4);
        for(User user : users) {
            Teacher teacher = user.getTeacher();
            if(teacher!=null) {
                res.add(teacher);
            }
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value="/t/update",method= RequestMethod.POST)
    public void updateTeacher(@RequestBody Teacher teacher){
        Teacher newTeacher = teacherDao.save(teacher);
        if(newTeacher!=null) {
            User user = new User();
            user.setRole(1);
            user.setTeacher(newTeacher);
            user.setUsername(newTeacher.getIdCode());
            userDao.save(user);
        }
    }

    @ResponseBody
    @RequestMapping(value="/t/auth/{authId}/update",method= RequestMethod.POST)
    public void updateTeacherWithAuth(@RequestBody Teacher teacher,@PathVariable Integer authId){
        Teacher newTeacher = teacherDao.save(teacher);
        if(newTeacher!=null) {
            User user = userDao.findByTeacher(newTeacher);
            if(user == null) user = new User();
            user.setRole(authId);
            user.setTeacher(newTeacher);
            user.setUsername(newTeacher.getIdCode());
            userDao.save(user);
        }
    }

    @ResponseBody
    @RequestMapping(value="/t/id/{username}",method= RequestMethod.GET)
    public Integer findTeacherId(@PathVariable String username){
        Teacher teacher = teacherDao.findByName(username);
        if(teacher != null){
            return teacher.getId();
        }
        return 0;
    }

    @RequestMapping(value="/t/all",method= RequestMethod.GET)
    public Iterable<Teacher> getAll(){
        return teacherDao.findByNameIsNotNullOrderByIdCode();
    }
}
