package cn.edu.shnu.fb.domain.major;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the major database table.
 *
 */
@Entity
public class Major implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private int id;

	private int grade;

	@Lob
	private String title;

	@Column(name="major_code")
	private String majorCode;

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

	public String getMajorCode() {
		return this.majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

}
