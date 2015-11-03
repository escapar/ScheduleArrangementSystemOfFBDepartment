package cn.edu.shnu.fb.domain.plan;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bytenoob on 15/11/1.
 */
public interface PlanCourseRepository extends PagingAndSortingRepository<PlanCourse,Integer> {
    List<PlanCourse> findPlanCoursesByPlan(Plan plan);
}
