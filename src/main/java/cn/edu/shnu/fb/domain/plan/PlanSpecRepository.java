package cn.edu.shnu.fb.domain.plan;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface PlanSpecRepository extends PagingAndSortingRepository<PlanSpec,Integer> {
    List<PlanSpec> findPlanSpecsByPlan(Plan plan);

}
