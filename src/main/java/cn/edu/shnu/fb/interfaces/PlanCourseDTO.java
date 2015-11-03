package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.plan.Plan;
import cn.edu.shnu.fb.domain.plan.PlanCourse;

/**
 * Created by bytenoob on 15/10/31.
 */
public class PlanCourseDTO {
    private int id;

    private float credits;

    private float period;

    private CourseTypeDTO courseType;

    private CourseClassDTO courseClass;

    private CourseExamDTO courseExam;

    private CourseDTO course;

    private PlanDTO plan;

    private int IsDegCourse;

    public PlanCourseDTO(PlanCourse planCourse){
        if(planCourse!=null) {
            id = planCourse.getId();
            credits = planCourse.getCredits();
            CourseType tmpCT=planCourse.getCourseType();
            CourseClass tmpCC=planCourse.getCourseClass();
            CourseExam tmpCE=planCourse.getCourseExam();
            Course tmpC=planCourse.getCourse();
            Plan tmpP =planCourse.getPlan();

            if(tmpCT!=null) {
                courseType = new CourseTypeDTO(tmpCT);
            }
            if(tmpCC!=null) {
                courseClass = new CourseClassDTO(tmpCC);
            }
            if(tmpCE!=null) {
                courseExam = new CourseExamDTO(tmpCE);
            }
            if(tmpC!=null){
                course = new CourseDTO(tmpC);
            }
            period = planCourse.getPeriod();
            IsDegCourse = planCourse.getIsDegCourse();
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

    public CourseExamDTO getCourseExam() {
        return courseExam;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public int getIsDegCourse() {
        return IsDegCourse;
    }

    public PlanDTO getPlan() {
        return plan;
    }

}
