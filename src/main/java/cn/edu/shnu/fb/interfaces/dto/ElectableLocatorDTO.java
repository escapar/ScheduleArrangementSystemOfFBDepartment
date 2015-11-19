package cn.edu.shnu.fb.interfaces.dto;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/18.
 */
public class ElectableLocatorDTO {
    public ElectableLocatorDTO(Locator locator) {
        this.id=locator.getId();
        this.term=locator.getTerm();
        this.courseClass=locator.getCourseClass();
        this.courseType=locator.getCourseType();
        this.modified=locator.getModified();
        this.major = locator.getMajor();
    }
    public ElectableLocatorDTO(){}

    public void setCreditsNeeded(final float credits) {
        this.creditsNeeded = credits;
    }

    private int id;

    public float getCreditsNeeded() {
        return creditsNeeded;
    }

    public int getId() {
        return id;
    }

    public Term getTerm() {
        return term;
    }

    public CourseClass getCourseClass() {
        return courseClass;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public int getModified() {
        return modified;
    }

    private Term term;

    private CourseClass courseClass;

    private CourseType courseType;

    public Major getMajor() {
        return major;
    }

    public void setMajor(final Major major) {
        this.major = major;
    }

    private Major major;

    public float getCreditsAchieved() {
        return creditsAchieved;
    }

    public void setCreditsAchieved(final float creditsAchieved) {
        this.creditsAchieved = creditsAchieved;
    }

    private float creditsAchieved;

    private int modified;

    float creditsNeeded;
}
