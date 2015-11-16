package cn.edu.shnu.fb.domain.plan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.course.Course;

/**
 * The persistent class for the plan_course database table.
 * 
 */
@Entity
@Table(name="plan_course")
public class PlanCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private float credits;

	@Column(name="is_deg_course")
	private int isDegCourse;

	private float period;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private Locator locator;

	//uni-directional many-to-one association to Course
	@ManyToOne
	private Course course;

	//uni-directional many-to-one association to CourseExam
	@ManyToOne
	@JoinColumn(name="course_exam_id")
	private CourseExam courseExam;

	public PlanCourse() {
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

	public int getIsDegCourse() {
		return this.isDegCourse;
	}

	public void setIsDegCourse(int isDegCourse) {
		this.isDegCourse = isDegCourse;
	}

	public float getPeriod() {
		return this.period;
	}

	public void setPeriod(float period) {
		this.period = period;
	}

	public Locator getLocator() {
		return this.locator;
	}

	public void setLocator(Locator locator) {
		this.locator = locator;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public CourseExam getCourseExam() {
		return this.courseExam;
	}

	public void setCourseExam(CourseExam courseExam) {
		this.courseExam = courseExam;
	}

}