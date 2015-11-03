package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.course.Course;

/**
 * Created by bytenoob on 15/10/31.
 */
public class CourseDTO {

    private int id;
    private String title;
    private String code;

    public CourseDTO(Course course){
        if(course!=null) {
            id = course.getId();
            code = course.getCode();
            title = course.getTitle();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

}
