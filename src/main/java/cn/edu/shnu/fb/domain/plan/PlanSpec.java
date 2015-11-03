package cn.edu.shnu.fb.domain.plan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;

/**
 * The persistent class for the plan_spec database table.
 * 
 */
@Entity
@Table(name="plan_spec")
@NamedQuery(name="PlanSpec.findAll", query="SELECT p FROM PlanSpec p")
public class PlanSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private float credits;

	private float period;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private Plan plan;

	@ManyToOne
	@JoinColumn(name="course_type_id")
	private CourseType courseType;

	@ManyToOne
	@JoinColumn(name="course_class_id")
	private CourseClass courseClass;

	public PlanSpec() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getCredits() {
		return this.credits;
	}

	public void setCredits(float credits) {
		this.credits = credits;
	}

	public float getPeriod() {
		return this.period;
	}

	public void setPeriod(float period) {
		this.period = period;
	}

	public Plan getPlan() {
		return this.plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public CourseType getCourseType() {
		return this.courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public CourseClass getCourseClass() {
		return this.courseClass;
	}

	public void setCourseClass(CourseClass courseClass) {
		this.courseClass = courseClass;
	}


}