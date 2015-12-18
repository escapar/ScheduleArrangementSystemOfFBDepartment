package cn.edu.shnu.fb.interfaces.dto;

import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.course.Course;

/**
 * Created by bytenoob on 15/12/13.
 */
public class RejectedCourseInspectionDTO {
    SalaryDTO salary;
    GridEntityDTO course;

    public RejectedCourseInspectionDTO(){

    }
    public RejectedCourseInspectionDTO(SalaryDTO s , GridEntityDTO c){
        salary = s;
        course = c;
    }
    public SalaryDTO getSalary() {
        return salary;
    }

    public void setSalary(final SalaryDTO salary) {
        this.salary = salary;
    }

    public GridEntityDTO getCourse() {
        return course;
    }

    public void setCourse(final GridEntityDTO course) {
        this.course = course;
    }
}
