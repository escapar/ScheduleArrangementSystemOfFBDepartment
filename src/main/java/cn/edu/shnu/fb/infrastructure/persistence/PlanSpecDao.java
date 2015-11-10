package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface PlanSpecDao extends PagingAndSortingRepository<PlanSpec,Integer> {
    List<PlanSpec> findByLocator(Locator locator);
    List<PlanSpec> findByLocatorMajor(Major major);
    List<PlanSpec> findByLocatorMajorAndLocatorTerm(Major major,Term term);
    List<PlanSpec> findByLocatorMajorAndLocatorTermAndLocatorCourseClass(Major major,Term term,CourseClass courseClass); // 只单独拿限选的时候用

}
