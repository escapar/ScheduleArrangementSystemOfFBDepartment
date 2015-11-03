package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.imp.ImpCourse;

/**
 * Created by bytenoob on 15/11/2.
 */
public class ImpCourseDTO {
    private int id;
    private CourseDTO course;
    private String courseClass;
    private String courseType;
    private float periodH;
    private float periodW;
    private float credits;
    private int impId;
    public ImpCourseDTO(ImpCourse impCourse){
        id = impCourse.getId();
        course = new CourseDTO(impCourse.getCourse());
        courseClass = impCourse.getCourseClass().getTitle();
        CourseType tmpCT = impCourse.getCourseType();
        if(tmpCT!=null){
            courseType = new CourseTypeDTO(tmpCT).getTitle();
        }
        periodH = impCourse.getPeriodHPerWeek();
        periodW = impCourse.getPeriodInWeek();
        credits = impCourse.getCredits();
        impId = impCourse.getImp().getId();
    }

    public int getId() {
        return id;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public String getCourseClass() {
        return courseClass;
    }

    public String getCourseType() {
        return courseType;
    }

    public float getPeriodH() {
        return periodH;
    }

    public float getPeriodW() {
        return periodW;
    }

    public float getCredits() {
        return credits;
    }

    public int getImpId() {
        return impId;
    }
}
