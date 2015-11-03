package cn.edu.shnu.fb.domain.imp;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * The persistent class for the imp database table.
 * 
 */
@Entity
@NamedQuery(name="Imp.findAll", query="SELECT i FROM Imp i")
public class Imp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String comment;

	@ManyToOne
	private Major major;

	@ManyToOne
	private Term term;

	@OneToMany(mappedBy="imp", fetch=FetchType.EAGER)
	private List<ImpCourse> impCourses;

	public Imp() {
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

	public Major getMajor() {
		return this.major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public Term getTerm() {
		return this.term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}


	public List<ImpCourse> getImpCourses() {
		return this.impCourses;
	}

	public void setImpCourses(List<ImpCourse> impCourses) {
		this.impCourses = impCourses;
	}

	public ImpCourse addImpCourse(ImpCourse ImpCourse) {
		getImpCourses().add(ImpCourse);
		ImpCourse.setImp(this);

		return ImpCourse;
	}

	public ImpCourse removeImpCourse(ImpCourse ImpCourse) {
		getImpCourses().remove(ImpCourse);
		ImpCourse.setImp(null);

		return ImpCourse;
	}

}