package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseType;

/**
 * Created by bytenoob on 15/10/31.
 */
public class CourseTypeDTO {
    private int id;


    private String title;
    private int classId;

    public CourseTypeDTO(CourseType courseType){
        if(courseType != null) {
            id = courseType.getId();
            title = courseType.getTitle();
            classId = courseType.getCourseClass().getId();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getClassId() {
        return classId;
    }
}
