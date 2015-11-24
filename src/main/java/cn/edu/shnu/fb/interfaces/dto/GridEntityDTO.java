package cn.edu.shnu.fb.interfaces.dto;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/1.
 */
public class GridEntityDTO {

    //For Backend Identity
    private int id;
    private int courseId;
    private int courseExamId;

    // CourseParams
    private String courseClass;

    private String code;
    private String title;
    private String courseExam;
    // Period
    private float[] period= new float[9]; // use 0 only if not a plan
    private float periodWeeks ;

    // Credits
    private float[] credits= new float[9];// use 0 only if not a plan

    //Comment
    private String courseComment;
    private int isDegCourse;


    private String teacherName;
    private String teacherProTitle;
    private String teacherCode;

    private int teacherId;
    private String comment;
    public GridEntityDTO(){

    }
    public GridEntityDTO(PlanCourse planCourse){
        if(planCourse!=null) {
            this.id = planCourse.getId();
            this.code = planCourse.getCourse().getCode();
            this.title = planCourse.getCourse().getTitle();
            this.courseId = planCourse.getCourse().getId();
            this.courseClass = planCourse.getLocator().getCourseClass().getTitle();
            this.period[0]=planCourse.getPeriod();
            this.credits[0]=planCourse.getCredits();
            this.isDegCourse = planCourse.getIsDegCourse();
            if(planCourse.getCourseExam()!=null) {
                this.courseExam = planCourse.getCourseExam().getTitle();
                this.courseExamId = planCourse.getCourseExam().getId();
            }
        }
    }
    public GridEntityDTO(PlanSpec planSpec){
        if(planSpec!=null) {
            this.id = planSpec.getId();
            this.courseClass = planSpec.getLocator().getCourseClass().getTitle();
            if (planSpec.getLocator().getCourseType() != null) {
                this.title = planSpec.getLocator().getCourseType().getTitle();
            }
            this.period[0] = planSpec.getPeriod();
            this.credits[0] = planSpec.getCredits();
        }
    }
    public GridEntityDTO(Imp imp){
        if(imp !=null){
            this.id= imp.getId();
            this.credits[0]= imp.getCredits();
            this.period[0]= imp.getPeriodHours();
            this.periodWeeks= imp.getPeriodWeeks();
            this.code= imp.getCourse().getCode();
            this.title= imp.getCourse().getTitle();
            this.courseId = imp.getCourse().getId();
            this.courseClass= imp.getLocator().getCourseClass().getTitle();
            this.isDegCourse = imp.getIsDegCourse();
            this.courseComment=imp.getCourseComment();
            if(imp.getCourseExam()!=null) {
                this.courseExam=imp.getCourseExam().getTitle();
                this.courseExamId=imp.getCourseExam().getId();
            }
            if(imp.getTeacher()!=null) {
                this.teacherName = imp.getTeacher().getName();
                this.teacherCode = imp.getTeacher().getIdCode();
                this.teacherProTitle = imp.getTeacher().getProTitle();
            }
            if(imp.getCourseComment()!=null && !imp.getCourseComment().isEmpty()){
                this.comment = imp.getCourseComment();
            }
        }
    }

    public void setPeriodAndCredits(Major major,PlanCourse planCourse){ // for a full grid of 8 terms
        int indice;
        Term term = planCourse.getLocator().getTerm();
        indice = (term.getYear() - major.getGrade()) * 2 + term.getPart()-1;
        if(indice>7) {          // 只有总学分 不在排课表上显示的情况
            this.period[0] = 0;
            this.credits[0] = 0;
            this.period[0] = 0;
            indice=8;
        }
        this.period[indice]=planCourse.getPeriod();
        this.credits[indice]=planCourse.getCredits();
    }
    public void setPeriodAndCredits(Major major,PlanSpec planSpec){ // for a full grid of 8 terms
        int indice;
        Term term = planSpec.getLocator().getTerm();
        indice = (term.getYear() - major.getGrade()) * 2 + term.getPart()-1;
        this.period[indice] = planSpec.getPeriod();
        this.credits[indice] = planSpec.getCredits();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(final String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherProTitle() {
        return teacherProTitle;
    }

    public void setTeacherProTitle(final String teacherProTitle) {
        this.teacherProTitle = teacherProTitle;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(final String teacherCode) {
        this.teacherCode = teacherCode;
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
    public float getPeriodWeeks() {
        return periodWeeks;
    }
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(final int courseId) {
        this.courseId = courseId;
    }
    public int getIsDegCourse() {
        return isDegCourse;
    }

    public void setIsDegCourse(final int isDegCourse) {
        this.isDegCourse = isDegCourse;
    }
    public int getCourseExamId() {
        return courseExamId;
    }

    public void setCourseExamId(final int courseExamId) {
        this.courseExamId = courseExamId;
    }

    public String getCourseExam() {
        return courseExam;
    }

    public void setCourseExam(final String courseExam) {
        this.courseExam = courseExam;
    }
    public String getCourseComment() {
        return courseComment;
    }

    public void setCourseComment(final String courseComment) {
        this.courseComment = courseComment;
    }

    public void setCourseClass(final String courseClass) {
        this.courseClass = courseClass;
    }


    public void setTitle(final String title) {
        this.title = title;
    }

    public void setCode(final String code) {
        this.code = code;
    }


    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(final int teacherId) {
        this.teacherId = teacherId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }
}
