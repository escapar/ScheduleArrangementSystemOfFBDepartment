package cn.edu.shnu.fb.domain.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.springframework.context.annotation.Lazy;

import cn.edu.shnu.fb.domain.Imp.Imp;

/**
 * The persistent class for the teacher database table.
 *
 */
@Entity

public class Teacher implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private int id;

	@Lob
	private String name;

	@Lob
	@Column(name="pro_title")
	private String proTitle;

	private String department;

	@Lob
	@Column(name="id_code")
	private String idCode;


	@ManyToMany(mappedBy="teachers")
	@JsonIgnore
	private List<Imp> imps;
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

	public String getProCode() {
		return idCode;
	}

	public void setProCode(final String idCode) {
		this.idCode = idCode;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public List<Imp> getImps() {
		return imps;
	}

	public void setImps(final List<Imp> imps) {
		this.imps = imps;
	}
}
