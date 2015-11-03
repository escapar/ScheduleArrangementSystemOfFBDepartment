package cn.edu.shnu.fb.domain.plan;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import cn.edu.shnu.fb.domain.major.Major;

public interface PlanRepository extends CrudRepository<Plan, Integer> {

    List<Plan> findPlansByMajor(Major major);
    List<Plan> findPlansByMajorAndTermYearAndTermPart(Major major , Integer year , Integer part);

}
