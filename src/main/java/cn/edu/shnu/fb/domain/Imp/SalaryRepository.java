package cn.edu.shnu.fb.domain.Imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.SalaryDao;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/8.
 */
@Repository
public class SalaryRepository {
    @Autowired
    SalaryDao salaryDao;

    public List<Salary> findByTeacherId(Integer teacherId){
        return salaryDao.findByTeacherId(teacherId);
    }
    public Salary persistSalary(SalaryDTO salaryDTO){
        Salary salary = new Salary(salaryDTO);
        return salaryDao.save(salary);
    }

    public Salary findById(Integer salaryId){
        return salaryDao.findOne(salaryId);
    }

    public void deleteById(Integer salaryId){
        Salary salary = findById(salaryId);
        salaryDao.delete(salary);
    }
    public Salary rejectSalary(Salary salary , String comment){
        if(salary!=null){
            salary.setRejected(1);
            salary.setRejectComment(comment);
            return salaryDao.save(salary);
        }
        return null;
    }

    public List<Salary> findRejectedSalary(){
        return salaryDao.findByRejected(1);
    }

    public Salary findDedection(Integer teacherId){
        return salaryDao.findByTeacherIdAndCourseTypeLike(teacherId, "扣%");
    }

}
