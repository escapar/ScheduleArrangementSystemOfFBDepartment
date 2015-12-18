package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/12/8.
 */
public interface SalaryDao extends PagingAndSortingRepository<Salary,Integer> {
    List<Salary> findByRejected(Integer rejected);
    Salary findByTeacherIdAndCourseTypeLike(Integer teacherId,String courseType);
}
