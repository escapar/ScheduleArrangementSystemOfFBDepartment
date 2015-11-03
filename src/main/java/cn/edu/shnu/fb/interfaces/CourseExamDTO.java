package cn.edu.shnu.fb.interfaces;

import cn.edu.shnu.fb.domain.common.CourseExam;

/**
 * Created by bytenoob on 15/10/31.
 */
public class CourseExamDTO {
    private int id;


    private String title;

    public CourseExamDTO(CourseExam courseExam){
        if(courseExam!=null) {
            id = courseExam.getId();
            title = courseExam.getTitle();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
