package cn.edu.shnu.fb.domain.major;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the major database table.
 * 
 */
@Entity
public class Major implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int grade;

	@Lob
	private String title;

	public Major() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGrade() {
		return this.grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}