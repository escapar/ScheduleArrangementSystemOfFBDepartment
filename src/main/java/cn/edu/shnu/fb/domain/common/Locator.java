package cn.edu.shnu.fb.domain.common;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.context.annotation.Lazy;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/6.
 */
@Entity
@Table(name="locator")
public class Locator implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;

    @ManyToOne
    private Major major;

    @ManyToOne
    private Term term;

    @ManyToOne
    @JoinColumn(name="course_class_id")
    private CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name="course_type_id")
    private CourseType courseType;

    private int modified;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Locator(){

    }


    public Locator(Major major , Term term , CourseClass courseClass , CourseType courseType){
        this.major = major;
        this.term = term;
        this.courseClass = courseClass;
        this.courseType = courseType;
    }

    public Locator(Major major , Term term , CourseClass courseClass ){
        this.major = major;
        this.term = term;
        this.courseClass = courseClass;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }




    public Major getMajor() {
        return major;
    }

    public void setMajor(final Major major) {
        this.major = major;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(final Term term) {
        this.term = term;
    }

    public CourseClass getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(final CourseClass courseClass) {
        this.courseClass = courseClass;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(final CourseType courseType) {
        this.courseType = courseType;
    }

    public int getModified() {
        return modified;
    }

    public void setModified(final int modified) {
        this.modified = modified;
    }
}
