package cn.edu.shnu.fb.interfaces.dto;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/12/8.
 */
public class SalaryDTO {
    private String courseType;
    private String majorType;
    private String departmentType;
    private String teacher;
    private String proTitle;
    private float salaryPerHour;
    private String majorTitle;
    private String courseTitle;
    private float periodHours;
    private int majorPopulation;
    private String location;
    private float underGraduateFactor;
    private float overseaStudentFactor;
    private float hanFactor;
    private float foreignLanguageFactor;
    private float populationFactor;
    private float suburbAllowance;
    private float firstCourseAllowance;
    private float basicSalaryDeduction;
    private float termSalary;
    private float monthlySalary;
    private int monthlySalaryRound;
    private String comment;

    public SalaryDTO(List<Imp> imps , String majorType , Teacher teacher , int state){
        //state 0 for normal , 1 for underGraduate , 2 for others(more to add..)
        //imps should be in same course , could be a set of merged course
        courseType = imps.get(0).getLocator().getCourseClass().getTitle();
        this.majorType = majorType;
        departmentType = teacher.getDepartment();
        this.teacher = teacher.getName();
        proTitle = teacher.getProTitle();
        salaryPerHour = getProTitleSalary(teacher);
        courseTitle = imps.get(0).getCourse().getTitle();
        periodHours = 0;
        majorPopulation = 0;
        for(Imp imp : imps) {
            majorTitle = appendString(majorTitle, imp.getLocator().getMajor().getMajorType().getTitle());
            majorPopulation += imp.getLocator().getMajor().getPopulation();
            periodHours += imp.getPeriodHours();
        }
        if(imps.get(0).getLocator().getMajor().getSuburb() == 1 ) {
            location = "奉贤";
            suburbAllowance = getSuburbAllowance(imps.get(0));
        }else{
            location = "徐汇";
            suburbAllowance = 0;
        }
        populationFactor = getPopulationFactor(majorPopulation);
        basicSalaryDeduction = getSalaryDeduction(teacher);
        if(salaryPerHour != 0) {
            underGraduateFactor = (float) (state == 1 ? 0.2 : 0);
            overseaStudentFactor = 0;
            hanFactor = 0;
            foreignLanguageFactor = 0;
            firstCourseAllowance = 0;
            float finalFactor = 1 + underGraduateFactor + overseaStudentFactor + hanFactor + foreignLanguageFactor + populationFactor;
            termSalary = ((salaryPerHour) * (periodHours - basicSalaryDeduction)) * finalFactor + suburbAllowance;
            monthlySalary = termSalary / 5;
            monthlySalaryRound = (int) monthlySalary;
        }
        comment = "";
    }

    float getSalaryDeduction(Teacher teacher){
        String type = teacher.getType();
        float period = 0;
        if(type.contains("教学岗")){
            period = 9;
        }else if(type.contains("科研岗")){
            period = 5;
        }else if(type.contains("教学科研岗")){
            period = 7;
        }
        return period;
    }

    public String appendString(String origin , String add){
        if(origin!=null && !origin.isEmpty()){
            return origin + ',' + add;
        }else{
            return add;
        }
    }

    float getPopulationFactor(int population){
        if(population<50){
            return 0;
        }else{
            int factor = (population-50)/10;
            float res = (float) (0.2 + 0.12* factor);
            return (float)(res > 0.8 ? 0.8 : res);
        }
    }
    float getSuburbAllowance(Imp imp){
        if(imp.getLocator().getMajor().getSuburb() != 0) {
            float hours = imp.getPeriodHours();
            float allowance = hours * 20;
            if (allowance < 50)
                return 50;
            else if (allowance > 120)
                return 120;
            else
                return allowance;
        }
        return 0;
    }

    float getProTitleSalary(Teacher teacher){
        String title = teacher.getProTitle();
        if(title == null || title.isEmpty()){
            return 0;
        }else if(title.contains("初级")){
            return 85;
        }else if(title.contains("中级")){
            return 95;
        }else if(title.contains("副高")){
            return 105;
        }else if(title.contains("正高")){
            return 115;
        }else{
            return 240;
        }
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(final String courseType) {
        this.courseType = courseType;
    }

    public String getMajorType() {
        return majorType;
    }

    public void setMajorType(final String majorType) {
        this.majorType = majorType;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(final String departmentType) {
        this.departmentType = departmentType;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(final String teacher) {
        this.teacher = teacher;
    }

    public String getProTitle() {
        return proTitle;
    }

    public void setProTitle(final String proTitle) {
        this.proTitle = proTitle;
    }

    public float getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(final float salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }

    public String getMajorTitle() {
        return majorTitle;
    }

    public void setMajorTitle(final String majorTitle) {
        this.majorTitle = majorTitle;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(final String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public float getPeriodHours() {
        return periodHours;
    }

    public void setPeriodHours(final float periodHours) {
        this.periodHours = periodHours;
    }

    public int getMajorPopulation() {
        return majorPopulation;
    }

    public void setMajorPopulation(final int majorPopulation) {
        this.majorPopulation = majorPopulation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public float getUnderGraduateFactor() {
        return underGraduateFactor;
    }

    public void setUnderGraduateFactor(final float underGraduateFactor) {
        this.underGraduateFactor = underGraduateFactor;
    }

    public float getOverseaStudentFactor() {
        return overseaStudentFactor;
    }

    public void setOverseaStudentFactor(final float overseaStudentFactor) {
        this.overseaStudentFactor = overseaStudentFactor;
    }

    public float getHanFactor() {
        return hanFactor;
    }

    public void setHanFactor(final float hanFactor) {
        this.hanFactor = hanFactor;
    }

    public float getForeignLanguageFactor() {
        return foreignLanguageFactor;
    }

    public void setForeignLanguageFactor(final float foreignLanguageFactor) {
        this.foreignLanguageFactor = foreignLanguageFactor;
    }

    public float getPopulationFactor() {
        return populationFactor;
    }

    public void setPopulationFactor(final float populationFactor) {
        this.populationFactor = populationFactor;
    }

    public float getSuburbAllowance() {
        return suburbAllowance;
    }

    public void setSuburbAllowance(final float suburbAllowance) {
        this.suburbAllowance = suburbAllowance;
    }

    public float getFirstCourseAllowance() {
        return firstCourseAllowance;
    }

    public void setFirstCourseAllowance(final float firstCourseAllowance) {
        this.firstCourseAllowance = firstCourseAllowance;
    }

    public float getBasicSalaryDeduction() {
        return basicSalaryDeduction;
    }

    public void setBasicSalaryDeduction(final float basicSalaryDeduction) {
        this.basicSalaryDeduction = basicSalaryDeduction;
    }

    public float getTermSalary() {
        return termSalary;
    }

    public void setTermSalary(final float termSalary) {
        this.termSalary = termSalary;
    }

    public float getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(final float monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public int getMonthlySalaryRound() {
        return monthlySalaryRound;
    }

    public void setMonthlySalaryRound(final int monthlySalaryRound) {
        this.monthlySalaryRound = monthlySalaryRound;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }
}
