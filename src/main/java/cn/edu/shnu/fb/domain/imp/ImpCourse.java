package cn.edu.shnu.fb.domain.imp;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.course.Course;

/**
 * The persistent class for the imp_course database table.
 *
 */
@Entity
@Table(name="imp_course")
@NamedQuery(name="ImpCourse.findAll", query="SELECT i FROM ImpCourse i")
public class ImpCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	@Lob
	private String comment;

	private float credits;

	@Column(name="is_deg_course")
	private int isDegCourse;

	@Column(name="period_h_per_week")
	private float periodHPerWeek;

	@Column(name="period_in_week")
	private float periodInWeek;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Imp imp;

	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="course_class_id")
	private CourseClass courseClass;

	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="course_type_id")
	private CourseType courseType;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Course course;

	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="course_exam_id")
	private CourseExam courseExam;

	public ImpCourse() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public float getPeriodHPerWeek() {
		return this.periodHPerWeek;
	}

	public void setPeriodHPerWeek(float periodHPerWeek) {
		this.periodHPerWeek = periodHPerWeek;
	}

	public float getPeriodInWeek() {
		return this.periodInWeek;
	}

	public void setPeriodInWeek(float periodInWeek) {
		this.periodInWeek = periodInWeek;
	}

	public CourseClass getCourseClass() {
		return this.courseClass;
	}

	public void setCourseClass(CourseClass courseClass) {
		this.courseClass = courseClass;
	}

	public CourseType getCourseType() {
		return this.courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
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

	public Imp getImp() {
		return this.imp;
	}

	public void setImp(Imp imp) {
		this.imp = imp;
	}

}