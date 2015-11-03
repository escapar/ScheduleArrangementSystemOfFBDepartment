package cn.edu.shnu.fb.domain.common;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the course_type database table.
 * 
 */
@Entity
@Table(name="course_type")
@NamedQuery(name="CourseType.findAll", query="SELECT c FROM CourseType c")
public class CourseType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String title;

	//uni-directional many-to-one association to CourseClass
	@ManyToOne
	@JoinColumn(name="course_class_id")
	private CourseClass courseClass;

	public CourseType() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CourseClass getCourseClass() {
		return this.courseClass;
	}

	public void setCourseClass(CourseClass courseClass) {
		this.courseClass = courseClass;
	}

}