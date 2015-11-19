package cn.edu.shnu.fb.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the teacher database table.
 * 
 */
@Entity
public class Teacher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private int id;

	@Lob
	private String name;

	@Lob
	@Column(name="pro_title")
	private String proTitle;

	@Lob
	@Column(name="id_code")
	private String idCode;

	public Teacher() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProTitle() {
		return proTitle;
	}

	public void setProTitle(final String proTitle) {
		this.proTitle = proTitle;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(final String idCode) {
		this.idCode = idCode;
	}
}