package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.plan.Plan;
import cn.edu.shnu.fb.domain.plan.PlanSpec;

/**
 * Created by bytenoob on 15/10/31.
 */
public class PlanSpecDTO {
    private int id;

    private float credits;

    private float period;

    private CourseTypeDTO courseType;

    private CourseClassDTO courseClass;

    private PlanDTO plan;

    public PlanSpecDTO(PlanSpec planSpecs){
        if(planSpecs!=null) {
            id = planSpecs.getId();
            credits = planSpecs.getCredits();
            period = planSpecs.getPeriod();
            CourseType tmpCT=planSpecs.getCourseType();
            Plan tmpP = planSpecs.getPlan();
            if(tmpCT!=null) {
                courseType = new CourseTypeDTO(tmpCT);
            }
            CourseClass tmpCC=planSpecs.getCourseClass();
            if(tmpCC!=null) {
                courseClass = new CourseClassDTO(tmpCC);
            }
            if(tmpP!=null) {
                plan = new PlanDTO(tmpP, false);
            }
        }
    }

    public int getId() {
        return id;
    }

    public float getCredits() {
        return credits;
    }

    public float getPeriod() {
        return period;
    }

    public CourseTypeDTO getCourseType() {
        return courseType;
    }

    public CourseClassDTO getCourseClass() {
        return courseClass;
    }

    public PlanDTO getPlan() {
        return plan;
    }
}
