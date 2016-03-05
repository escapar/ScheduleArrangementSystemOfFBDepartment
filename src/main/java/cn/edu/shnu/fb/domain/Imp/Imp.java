package cn.edu.shnu.fb.domain.Imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.springframework.data.annotation.Transient;

import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.mergedClass.MergedClass;
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
	@Column(name="merge_comment")
	private String mergeComment;

	@Column(name="course_comment")
	private String courseComment;

	@JsonIgnore
	@ManyToOne
	private Salary salary;

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(final List<Teacher> teachers) {
		this.teachers = teachers;
	}

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(
			name = "imp_teacher",
			joinColumns = @JoinColumn(name = "imp_id"),
			inverseJoinColumns = @JoinColumn(name = "teacher_id")
	)
	private List<Teacher> teachers = new ArrayList<>();

	@ManyToOne(cascade= CascadeType.MERGE)
	private Locator locator;

	@ManyToOne(cascade= CascadeType.MERGE)
	private Course course;


	@ManyToOne(cascade= CascadeType.MERGE)
	@JoinColumn(name="course_exam_id")
	private CourseExam courseExam;

	//bi-directional many-to-one association to MergedClass
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name="merged_class_id")
	private MergedClass mergedClass;

	/*public void addTeacher(Teacher t){
		teachers.add(t);
	}*/

	public Imp() {
		teachers = new ArrayList<>();
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

	public String getCourseComment() {
		return this.courseComment;
	}

	public void setCourseComment(String courseComment) {
		this.courseComment=courseComment;
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
	public MergedClass getMergedClass() {
		return this.mergedClass;
	}

	public void setMergedClass(MergedClass mergedClass) {
		this.mergedClass = mergedClass;
	}

	public void setMergeComment(final String mergeComment) {
		this.mergeComment = mergeComment;
	}

	public String getMergeComment() {
		return mergeComment;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(final Salary salary) {
		this.salary = salary;
	}
}
