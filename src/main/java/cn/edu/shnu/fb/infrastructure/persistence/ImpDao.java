package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.mergedClass.MergedClass;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface ImpDao extends PagingAndSortingRepository<Imp,Integer> {
    List<Imp> findByLocator(Locator locator);
    Imp findByLocatorAndCourse(Locator locator , Course course);
    List<Imp> findByLocatorMajor(Major major);
    List<Imp> findByLocatorMajorAndLocatorTerm(Major major,Term term);
    List<Imp> findByLocatorMajorAndLocatorTermAndLocatorCourseClass(Major major,Term term,CourseClass courseClass); // 只单独拿限选的时候用
    List<Imp> findByLocatorMajorAndLocatorCourseClass(Major major,CourseClass courseClass);
    List<Imp> findByLocatorMajorAndLocatorCourseClassAndLocatorCourseType(Major major,CourseClass courseClass,CourseType courseType); // 只单独拿限选的时候用
    List<Imp> findBySalary(Salary salary);
    List<Imp> findByMergedClass(MergedClass mergedClass);
    List<Imp> findBySalaryRejected(Integer rejected);
    List<Imp> findBySalaryRejectedAndLocatorTerm(Integer rejected , Term term);
}
