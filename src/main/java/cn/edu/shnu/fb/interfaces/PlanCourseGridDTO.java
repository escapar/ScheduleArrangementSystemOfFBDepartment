package cn.edu.shnu.fb.interfaces;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/1.
 */
public class PlanCourseGridDTO {
    private int id;
    private String code;
    private String title;
    private float[] period= new float[8];
    private float periodW ;
    private float[] credits= new float[8];
    private String courseClass;
    public PlanCourseGridDTO(){

    }
    public PlanCourseGridDTO(PlanCourseDTO planCourseDTO){
        this.id = planCourseDTO.getId();
        this.code = planCourseDTO.getCourse().getCode();
        this.title=planCourseDTO.getCourse().getTitle();
        this.courseClass=planCourseDTO.getCourseClass().getTitle();
    }
    public PlanCourseGridDTO(PlanSpecDTO planSpecDTO){
        if(planSpecDTO!=null) {
            this.id = planSpecDTO.getId();
            this.courseClass = planSpecDTO.getCourseClass().getTitle();
            if (planSpecDTO.getCourseType() != null) {
                this.title = planSpecDTO.getCourseType().getTitle();
            }
        }
    }
    public PlanCourseGridDTO(ImpCourseDTO impCourseDTO){
        if(impCourseDTO!=null){
            this.id=impCourseDTO.getId();
            this.credits[0]=impCourseDTO.getCredits();
            this.period[0]=impCourseDTO.getPeriodH();
            this.periodW=impCourseDTO.getPeriodW();
            this.code=impCourseDTO.getCourse().getCode();
            this.title=impCourseDTO.getCourse().getTitle();
            this.courseClass=impCourseDTO.getCourseClass();
        }
    }
    public void setPeriodAndCredits(Major major,PlanCourseDTO planCourseDTO){
        int indice;
        Term term = planCourseDTO.getPlan().getTerm();
        indice = (term.getYear() - major.getGrade()) * 2 + term.getPart()-1;
        this.period[indice]=planCourseDTO.getPeriod();
        this.credits[indice]=planCourseDTO.getCredits();
    }
    public void setPeriodAndCredits(Major major,PlanSpecDTO planSpecDTO){
        int indice;
        Term term = planSpecDTO.getPlan().getTerm();
        indice = (term.getYear() - major.getGrade()) * 2 + term.getPart()-1;
        if(planSpecDTO.getPeriod()!=0) {
            this.period[indice] = planSpecDTO.getPeriod();
        }
        if(planSpecDTO.getCredits()!=0) {
            this.credits[indice] = planSpecDTO.getCredits();
        }
    }


    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setPeriod(float period[]) {
        this.period=period;
    }

    public void setCredits(float credits[]) {
        this.credits=credits;
    }

    public void setOnePeriod(float period) {
        this.period[0]=period;
    }

    public void setOneCredits(float credits) {
        this.credits[0]=credits;
    }

    public String getTitle() {
        return title;
    }

    public float[] getPeriod() {
        return period;
    }

    public float[] getCredits() {
        return credits;
    }

    public String getCourseClass() {
        return courseClass;
    }
    public float getPeriodW() {
        return periodW;
    }
}
