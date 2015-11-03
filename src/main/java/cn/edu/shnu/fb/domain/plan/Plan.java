package cn.edu.shnu.fb.domain.plan;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.context.annotation.Lazy;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * The persistent class for the plan database table.
 * 
 */
@Entity
public class Plan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@ManyToOne
	private Major major;

	@ManyToOne
	private Term term;

	@OneToMany(mappedBy="plan", fetch = FetchType.LAZY )
	@Lazy(true)
	private List<PlanCourse> planCourses;

	@OneToMany(mappedBy="plan", fetch = FetchType.LAZY )
	@Lazy(true)
	private List<PlanSpec> planSpecs;

	public Plan() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<PlanCourse> getPlanCourses() {
		return this.planCourses;
	}

	public void setPlanCourses(List<PlanCourse> planCourses) {
		this.planCourses = planCourses;
	}

	public PlanCourse addPlanCourse(PlanCourse planCourse) {
		getPlanCourses().add(planCourse);
		planCourse.setPlan(this);

		return planCourse;
	}

	public PlanCourse removePlanCourse(PlanCourse planCourse) {
		getPlanCourses().remove(planCourse);
		planCourse.setPlan(null);

		return planCourse;
	}

	public List<PlanSpec> getPlanSpecs() {
		return this.planSpecs;
	}

	public void setPlanSpecs(List<PlanSpec> planSpecs) {
		this.planSpecs = planSpecs;
	}

	public PlanSpec addPlanSpec(PlanSpec planSpec) {
		getPlanSpecs().add(planSpec);
		planSpec.setPlan(this);

		return planSpec;
	}

	public PlanSpec removePlanSpec(PlanSpec planSpec) {
		getPlanSpecs().remove(planSpec);
		planSpec.setPlan(null);

		return planSpec;
	}

	public void fillInDetail(){
		this.planCourses.size();
		this.planSpecs.size();
	}
}