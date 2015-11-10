package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/1.
 */
public interface PlanCourseDao extends PagingAndSortingRepository<PlanCourse,Integer> {
    List<PlanCourse> findByLocator(Locator locator);
    List<PlanCourse> findByLocatorMajor(Major major);
    List<PlanCourse> findByLocatorMajorAndLocatorTerm(Major major,Term term);
    List<PlanCourse> findByLocatorMajorAndLocatorCourseClass(Major major,CourseClass courseClass);
    List<PlanCourse> findByLocatorMajorAndAndLocatorTermAndLocatorCourseClass(Major major,Term term , CourseClass courseClass);

}
