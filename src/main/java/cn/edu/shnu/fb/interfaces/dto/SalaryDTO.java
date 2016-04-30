package cn.edu.shnu.fb.interfaces.dto;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/12/8.
 */
public class SalaryDTO {
    private String courseType;
    private int majorId;
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
    private int rejected;
    private String rejectComment;
    private String split;
    private String mergedStatus="否";

    private int teacherId;
    private int id;
    private List<Integer> impId = new ArrayList<>();

    public SalaryDTO(){

    }
    public SalaryDTO(List<Imp> imps , String majorType , Teacher teacher , int state){
        //state 0 for normal , 1 for underGraduate , 2 for others(more to add..)
        //imps should be in same course , could be a set of merged course
        courseType = imps.get(0).getLocator().getCourseClass().getTitle();
        majorId = imps.get(0).getLocator().getMajor().getId();
        this.majorType = majorType;
        departmentType = teacher.getDepartment();
        this.teacher = teacher.getName();
        proTitle = teacher.getProTitle();
        teacherId = teacher.getId();
        salaryPerHour = getProTitleSalary(teacher);
        courseTitle = imps.get(0).getCourse().getTitle();
        periodHours = 0;
        majorPopulation = 0;
        for(Imp imp : imps) {
            impId.add(imp.getId());
            if(imp.getSalary()!=null) {
                id = imp.getSalary().getId();
            }
            majorTitle = appendString(majorTitle, Integer.toString(imp.getLocator().getMajor().getGrade()) + '级' + imp.getLocator().getMajor().getMajorType().getTitle());
            majorPopulation += imp.getLocator().getMajor().getPopulation();
            //periodHours += imp.getPeriodHours();
            /*if(imp.getRejected()==1){
                rejected = true;
            }*/
        }
        periodHours = imps.get(0).getPeriodHours();
        if(imps.get(0).getMergedClass()!=null){
            mergedStatus = "是";
        }
        if(imps.get(0).getLocator().getMajor().getSuburb() == 1 ) {
            location = "奉贤";
            suburbAllowance = getSuburbAllowance(periodHours);
        }else{
            location = "徐汇";
            suburbAllowance = 0;
        }
        populationFactor = getPopulationFactor(majorPopulation);
        if(salaryPerHour != 0) {
            underGraduateFactor = (float) (state == 1 ? 0.2 : 0);
            overseaStudentFactor = 0;
            hanFactor = 0;
            foreignLanguageFactor = 0;
            firstCourseAllowance = 0;
            float finalFactor = 1 + underGraduateFactor + overseaStudentFactor + hanFactor + foreignLanguageFactor + populationFactor;
            termSalary = ((salaryPerHour) * (periodHours)) * finalFactor + suburbAllowance;
            monthlySalary = termSalary / 5;
            monthlySalaryRound = getRound(monthlySalary);
        }
        comment = " ";
        split=" ";

    }

    public SalaryDTO(Teacher teacher){
        //state 0 for normal , 1 for underGraduate , 2 for others(more to add..) , 99 for adjustment , 98 for sum up
        //imps should be in same course , could be a set of merged course
        teacherId = teacher.getId();
        this.teacher = teacher.getName();
        proTitle = teacher.getProTitle();
        courseType = "扣基本课时费";
        departmentType = teacher.getDepartment();
        this.teacher = teacher.getName();
        proTitle = teacher.getProTitle();
        salaryPerHour = getProTitleSalary(teacher);

        basicSalaryDeduction = - (getSalaryDeduction(teacher) * salaryPerHour);
        if(basicSalaryDeduction>0) basicSalaryDeduction=-basicSalaryDeduction;
        if(salaryPerHour != 0) {
            termSalary = (basicSalaryDeduction);
            monthlySalary = termSalary / 5;
            monthlySalaryRound = getRound(monthlySalary);
        }
        comment = " ";
        split=" ";
        majorType="";
        majorTitle="";
        courseTitle="";
        location="";

    }

    public SalaryDTO(List<SalaryDTO> dtos){
        //state 0 for normal , 1 for underGraduate , 2 for others(more to add..) , 99 for adjustment , 98 for sum up
        //imps should be in same course , could be a set of merged course
        courseType = "总计";
        departmentType = dtos.get(0).getDepartmentType();
        teacher = dtos.get(0).getTeacher();
        proTitle = dtos.get(0).getProTitle();
        salaryPerHour = dtos.get(0).getSalaryPerHour();
        for(SalaryDTO dto : dtos) {
            termSalary += dto.getTermSalary();
        }
        monthlySalary = termSalary / 5;
        monthlySalaryRound = getRound(monthlySalary);
        comment = " ";
        split=" ";
        majorType="";
        majorTitle="";
        courseTitle="";
        location="";
    }

    public SalaryDTO(Salary salaryDTO , Teacher teacher , Integer impId){
        this.impId.add(impId);
        id = salaryDTO.getId();
        proTitle = teacher.getProTitle();
        rejectComment = salaryDTO.getRejectComment();
        rejected = salaryDTO.getRejected();
        this.teacher = teacher.getName();
        majorTitle = salaryDTO.getMajorTitle();
        courseType = salaryDTO.getCourseType();
        majorType = salaryDTO.getMajorType();
        departmentType = salaryDTO.getDepartmentType();
        teacherId = salaryDTO.getTeacherId();
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
        split=salaryDTO.getSplit();
        if(majorTitle!=null && majorTitle.contains(",")){
            mergedStatus = "是";
        }
        if(split == null || split.isEmpty()){
            split = " ";
        }


    }

    public int getRound(float value){
        if(value - (int)value >= 0.5){
            return (int)value+1;
        }else{
            return (int)value;
        }
    }
    public float getSalaryDeduction(Teacher teacher){
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

    public float getPopulationFactor(int population){
        if(population<50){
            return 0;
        }else{
            int factor = (population-50)/10;
            float res = (float) (0.2 + 0.12* factor);
            return (float)(res > 0.8 ? 0.8 : res);
        }
    }
    public float getSuburbAllowance(float hours){
            float allowance = hours * 20;
            if (allowance < 50)
                return 50;
            else if (allowance > 120)
                return 120;
            else
                return allowance;
    }

    public float getProTitleSalary(Teacher teacher){
        String title = teacher.getProTitle();
        if(title == null || title.isEmpty()){
            return 1;
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

    public void setTeacherEntity(Teacher teacher){
        this.teacher = teacher.getName();
        this.departmentType = teacher.getDepartment();
        this.proTitle = teacher.getProTitle();
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

    public void setBasicSalaryDeduction(float basicSalaryDeduction) {
        if(basicSalaryDeduction >0) basicSalaryDeduction=-basicSalaryDeduction;
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
        this.setMonthlySalaryRound(getRound(monthlySalary));
    }

    public int getMonthlySalaryRound() {
        return getRound(monthlySalary);
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

    public int getRejected() {
        return rejected;
    }

    public void setRejected(final int rejected) {
        this.rejected = rejected;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getImpId() {
        return impId;
    }

    public void setImpId(final List<Integer> impId) {
        this.impId = impId;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getRejectComment() {
        return rejectComment;
    }

    public void setRejectComment(final String rejectComment) {
        this.rejectComment = rejectComment;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(final int majorId) {
        this.majorId = majorId;
    }

    public String getMergedStatus() {
        return mergedStatus;
    }


}
