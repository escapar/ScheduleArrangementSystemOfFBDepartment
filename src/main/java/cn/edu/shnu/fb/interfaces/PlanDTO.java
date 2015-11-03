package cn.edu.shnu.fb.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.Plan;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/10/31.
 */
public class PlanDTO implements Serializable {

    private int id;
    private Major major;
    private Term term;
    private List<PlanCourseDTO> planCourses = new ArrayList();
    private List<PlanSpecDTO> planSpecs = new ArrayList();
    public PlanDTO(Plan plan , boolean requestDetailedPlan){
        if(plan!=null) {
            if (requestDetailedPlan) {
                //Lazy Load Here
                List<PlanSpec> planSpecsImported = plan.getPlanSpecs();
                List<PlanCourse> planCoursesImported = plan.getPlanCourses();
                for (PlanSpec ps : planSpecsImported) {
                    if(ps!=null) {
                        planSpecs.add(new PlanSpecDTO(ps));
                    }
                }
                for (PlanCourse pc : planCoursesImported) {
                    if(pc!=null) {
                        planCourses.add(new PlanCourseDTO(pc));
                    }
                }
            }
            this.id = plan.getId();
            this.term = plan.getTerm();
            this.major = plan.getMajor();
        }
    }
    public int getId() {
        return id;
    }

    public Major getMajor() {
        return major;
    }

    public Term getTerm() {
        return term;
    }

    public List<PlanCourseDTO> getPlanCourses() {
        return planCourses;
    }

    public List<PlanSpecDTO> getPlanSpecDTOs() {
        return planSpecs;
    }
}
