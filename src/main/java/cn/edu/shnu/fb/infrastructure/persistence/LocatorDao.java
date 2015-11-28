package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/7.
 */
public interface LocatorDao extends PagingAndSortingRepository<Locator,Integer> {
    List<Locator> findByMajorAndTerm(Major major , Term term);
    List<Locator> findByCourseClass(CourseClass courseClass);
    List<Locator> findByCourseClassAndCourseType(CourseClass courseClass,CourseType courseType);
    List<Locator> findByMajorAndTermAndCourseClass(Major major , Term term , CourseClass courseClass);
    List<Locator> findByMajorAndTermAndCourseType(Major major,Term term,CourseType courseType);
    List<Locator> findByMajorAndCourseClass(Major major , CourseClass courseClass);
    List<Locator> findByMajor(Major major);
    Locator findByMajorAndTermAndCourseClassAndCourseType(Major major , Term term , CourseClass courseClass,CourseType courseType);



}
