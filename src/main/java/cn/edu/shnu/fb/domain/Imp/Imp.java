package cn.edu.shnu.fb.domain.Imp;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * The persistent class for the imp_course database table.
 *
 */
@Entity
@Table(name="imp")
public class Imp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	private float credits;

	@Column(name="is_deg_course")
	private int isDegCourse;

	@Column(name="period_weeks")
	private float periodWeeks;

	@Column(name="period_hours")
	private float periodHours;

	@Lob
	private String courseComment;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Locator locator;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Course course;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Teacher teacher;

	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="course_exam_id")
	private CourseExam courseExam;

	public Imp() {
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

	public float getPeriodHours() {
		return this.periodHours;
	}

	public void setPeriodHours(float periodHours) {
		this.periodHours = periodHours;
	}

	public float getPeriodWeeks() {
		return this.periodWeeks;
	}

	public void setPeriodWeeks(float periodWeeks) {
		this.periodWeeks = periodWeeks;
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

	public Locator getLocator() {
		return this.locator;
	}

	public void setLocator(Locator locator) {
		this.locator = locator;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}

	public String getCourseComment() {
		return courseComment;
	}

	public void setCourseComment(final String comment) {
		this.courseComment = comment;
	}
}