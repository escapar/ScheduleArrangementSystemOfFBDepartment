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

	private int population;

	@ManyToOne
	@JoinColumn(name="major_type_id")
	private MajorType majorType;

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

	public MajorType getMajorType() {
		return this.majorType;
	}

	public void setMajorType(MajorType majorType) {
		this.majorType = majorType;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(final int population) {
		this.population = population;
	}
}
