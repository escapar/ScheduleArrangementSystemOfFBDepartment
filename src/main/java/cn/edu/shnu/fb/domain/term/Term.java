package cn.edu.shnu.fb.domain.term;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the term database table.
 * 
 */
@Entity
public class Term implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private int id;

	private int part;

	private int weeks;

	private int year;

	public Term() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPart() {
		return this.part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getWeeks() {
		return this.weeks;
	}

	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}