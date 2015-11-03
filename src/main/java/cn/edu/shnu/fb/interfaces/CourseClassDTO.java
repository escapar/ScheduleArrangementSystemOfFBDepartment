package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseClass;

/**
 * Created by bytenoob on 15/10/31.
 */
public class CourseClassDTO {
    private int id;


    private String title;

    public CourseClassDTO(CourseClass courseClass){
        if(courseClass != null) {
            id = courseClass.getId();
            title=courseClass.getTitle();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
