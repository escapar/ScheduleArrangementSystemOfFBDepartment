package cn.edu.shnu.fb.domain.Imp;

import javax.persistence.Entity;
import javax.persistence.Id;

import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/8.
 */
@Entity

public class Salary {
    @Id
   // @JoinColumn(name="imp_id")
    private int impId;
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

    Salary(){

    }

    public Salary(Integer impId , SalaryDTO salaryDTO){
        this.impId = impId;
        courseType = salaryDTO.getCourseType();
        majorType = salaryDTO.getMajorType();
        departmentType = salaryDTO.getDepartmentType();
        teacher = salaryDTO.getTeacher();
        proTitle = salaryDTO.getProTitle();
        salaryPerHour = salaryDTO.getSalaryPerHour();
        majorType = salaryDTO.getMajorType();
        courseTitle = salaryDTO.getCourseTitle();
        periodHours = salaryDTO.getPeriodHours();
        majorPopulation = salaryDTO.getMajorPopulation();
        location = salaryDTO.getLocation();
        underGraduateFactor = salaryDTO.getUnderGraduateFactor();
        overseaStudentFactor = salaryDTO.getOverseaStudentFactor();
        hanFactor = salaryDTO.getHanFactor();
        foreignLanguageFactor = salaryDTO.getForeignLanguageFactor();
        populationFactor = salaryDTO.getPopulationFactor();
        suburbAllowance = salaryDTO.getSuburbAllowance();
        firstCourseAllowance = salaryDTO.getFirstCourseAllowance();
        basicSalaryDeduction = salaryDTO.getBasicSalaryDeduction();
        termSalary = salaryDTO.getTermSalary();
        monthlySalary = salaryDTO.getMonthlySalary();
        monthlySalaryRound = salaryDTO.getMonthlySalaryRound();
        comment = salaryDTO.getComment();
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

    public int getImpId() {
        return impId;
    }
}
