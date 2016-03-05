package cn.edu.shnu.fb.domain.plan;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.mergedClass.MergedClass;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseExamDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MergedClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanSpecDao;
import cn.edu.shnu.fb.infrastructure.persistence.SalaryDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class PlanRepository {

    @Autowired
    PlanCourseDao planCourseDao;

    @Autowired
    SalaryDao salaryDao;

    @Autowired
    MergedClassDao mergedClassDao;

    @Autowired
    CourseExamDao courseExamDao;

    @Autowired
    PlanSpecDao planSpecDao;

    @Autowired
    MajorDao majorDao;

    @Autowired
    LocatorDao locatorDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    CourseClassDao courseClassDao;
    @Autowired
    TermRepository termRepository;

    @Autowired
    CourseTypeDao courseTypeDao;

    @Autowired
    LocatorRepository locatorRepository;

    @Autowired
    ImpRepository impRepository;
    public List<PlanCourse> getPlanCoursesByLocator(Locator locator){
        return planCourseDao.findByLocator(locator);
    }
    public List<PlanCourse> getPlanCourseByLocatorId(int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        if(locator!=null) {
            return planCourseDao.findByLocator(locator);
        } else {
            return null;
        }
    }

    public PlanSpec getPlanSpecByLocator(Locator locator){
        return planSpecDao.findByLocator(locator);
    }

    public Iterable<PlanCourse> getPlanCoursesByMajorIdAndTermCount(int majorId , int termCount){
        if(termCount>0 && termCount<9) {
            List<PlanCourse> resList = new ArrayList<>();
            Major major = majorDao.findOne(majorId);
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term != null && major!=null) {
                resList = planCourseDao.findByLocatorMajorAndLocatorTerm(major,term);
            }
            return resList;
        }else{
            return null;
        }
    }

    public List<PlanSpec> getPlanSpecsByMajorId(int majorId){
        Major major = majorDao.findOne(majorId);
        if(major!=null){
            List<PlanSpec> planSpecs = planSpecDao.findByLocatorMajor(major);
            return planSpecs;
        }else{
            return null;
        }
    }

    public List<PlanCourse> getPlanCoursesByMajorId(int majorId){
        Major major = majorDao.findOne(majorId);
        if(major!=null) {
            List<PlanCourse> planCourses = planCourseDao.findByLocatorMajor(major);
            return planCourses;
        }else{
            return null;
        }
    }

    public List<PlanCourse> getObligePlanCoursesByMajorId(int majorId){
        Major major = majorDao.findOne(majorId);
        Iterable<CourseClass> courseClasses = courseClassDao.findByTitleNotLike("%选修%");
        List<PlanCourse> planCourses = new ArrayList<>();
        if(major!=null && courseClasses!=null) {
            for (CourseClass cc : courseClasses) {
                planCourses.addAll(planCourseDao.findByLocatorMajorAndLocatorCourseClass(major, cc));
            }
            return planCourses;
        }else{
            return null;
        }
    }

    public List<PlanCourse> getObligePlanCoursesByMajorIdAndTermCount(int majorId,int termCount){
        if(termCount>0 && termCount<9) {
            Major major = majorDao.findOne(majorId);
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            Iterable<CourseClass> courseClasses = courseClassDao.findByTitleNotLike("%选修%");
            List<PlanCourse> planCourses = new ArrayList<>();
            if(major!=null && courseClasses!=null && term!=null) {
                for (CourseClass cc : courseClasses) {
                    planCourses.addAll(planCourseDao.findByLocatorMajorAndAndLocatorTermAndLocatorCourseClass(major, term, cc));
                }
                return planCourses;
            }else{
                return null;
        }
        }
        else{
            return null;
        }
    }

    public List<PlanSpec> getPlanSpecsByMajorIdAndTermCount(int majorId,int termCount){
        if(termCount>0 && termCount<9) {
            Major major = majorDao.findOne(majorId);
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(major!=null && term!=null) {
                return planSpecDao.findByLocatorMajorAndLocatorTerm(major,term);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public PlanSpec getPlanSpecsByLocatorId(int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        if(locator!=null){
            return planSpecDao.findByLocator(locator);
        }else{
            return null;
        }
    }

    public void updatePlanCoursesByWordAndMajorId(int majorId,List<GridEntityDTO> geDTOs){
        Major major = majorDao.findOne(majorId);
        if (major != null) {
            for (GridEntityDTO geDTO : geDTOs) {
                //COURSE
                Course course = courseDao.findByCodeAndTitle(geDTO.getCode(), geDTO.getTitle());
                if (course == null) {
                    course = new Course();
                    if(geDTO.getCode()!=null) {
                        course.setCode(geDTO.getCode());
                    }
                    if(geDTO.getTitle()!=null) {
                        course.setTitle(geDTO.getTitle());
                    }
                    courseDao.save(course);
                    course = courseDao.findByCodeAndTitle(geDTO.getCode(), geDTO.getTitle());
                }

                //COURSECLASS COURSETYPE
                String cc = geDTO.getCourseClass();
                CourseType courseType = null;
                CourseClass courseClass = null;
                if(!(cc.contains("学院素质教育课程") || cc.contains("创新创业教育课程")) && !cc.contains("任选")){
                    courseClass= courseClassDao.findByTitleLike("限定选修课").get(0);
                    courseType= courseTypeDao.findByTitleLike("专业限定选修课");
                }else if(cc.contains("学院素质教育课程") || cc.contains("创新创业教育课程")){
                    courseClass= courseClassDao.findByTitleLike("限定选修课").get(0);
                    courseType= courseTypeDao.findByTitleLike(cc);
                }else{
                    courseClass= courseClassDao.findByTitleLike("任意选修课").get(0);
                }

                //LOCATOR
                int ctid = 0;
                if(courseType != null){
                    ctid=courseType.getId();
                }
                Locator locator = locatorRepository.getLocatorByMajorIdAndCourseClassIdAndCourseTypeId(majorId, courseClass.getId(),ctid);
                if (locator == null) {
                    locator = new Locator();
                    locator.setCourseClass(courseClass);
                    locator.setCourseType(courseType);
                    locator.setMajor(major);
                    locatorRepository.save(locator);
                    locator = locatorRepository.getLocatorByMajorIdAndCourseClassIdAndCourseTypeId(majorId, courseClass.getId(),ctid);
                }

                //PLANCOURSE
                PlanCourse planCourse = planCourseDao.findByCourseAndLocator(course,locator);
                if(planCourse == null) {
                    planCourse = new PlanCourse();
                    planCourse.setLocator(locator);
                    planCourse.setCourse(course);
                }
                    planCourse.setCredits(geDTO.getCredits()[0]);
                    planCourse.setPeriod(geDTO.getPeriod()[0]);

                    if (geDTO.getCourseExamId() != 0) {
                        CourseExam ce = courseExamDao.findOne(geDTO.getCourseExamId());
                        planCourse.setCourseExam(ce);
                    }

                planCourseDao.save(planCourse);
            }
        }
    }
    public void updatePlansByExcelAndMajorId(int majorId,List<GridEntityDTO> geDTOs) {
        Major major = majorDao.findOne(majorId);
        List<Locator> locators = locatorDao.findByMajor(major);
        for(Locator locator : locators){
            List<Imp> imps = impRepository.getImpByLocatorId(locator.getId());
            List<PlanCourse> planCourses = planCourseDao.findByLocator(locator);
            PlanSpec planSpec = planSpecDao.findByLocator(locator);
            for(Imp imp : imps){
                Salary salary = imp.getSalary();
                if(salary!=null) {
                    salaryDao.delete(salary);
                }
                MergedClass mergedClass = imp.getMergedClass();
                if(mergedClass!=null) {
                    mergedClassDao.delete(mergedClass);
                }
                impRepository.deleteImp(imp);
            }
            for(PlanCourse planCourse : planCourses){
                planCourseDao.delete(planCourse);
            }
            if(planSpec!=null) {
                planSpecDao.delete(planSpec);
            }
        }
        if (major != null) {
            for (GridEntityDTO geDTO : geDTOs) {
                if(geDTO.getCourseClass().contains("总计")){
                    break;
                }
                String title = geDTO.getTitle().replace("*","");
                    List<CourseClass> ccs = courseClassDao.findByTitleLike(geDTO.getCourseClass());

                    if (ccs.size() > 0) {
                        CourseClass cc = ccs.get(0);
                        if (!geDTO.getCourseClass().contains("选修")) { //必修
                            Course course = courseDao.findByCodeAndTitle(geDTO.getCode(), title);
                            if (course == null) {
                                course = new Course();
                                if(geDTO.getCode()!=null) {
                                    course.setCode(geDTO.getCode());
                                }
                                if(title!=null) {
                                    course.setTitle(title);
                                }
                                courseDao.save(course);
                                course = courseDao.findByCodeAndTitle(geDTO.getCode(), title);
                            }
                            for (int i = 0; i < 9; i++) {
                                if (geDTO.getPeriod()[i] != 0 || geDTO.getCredits()[i] != 0) {
                                    Locator locator ;
                                    Term term;
                                    Integer count = 0;
                                    if(geDTO.getTitle()!=null && geDTO.getTitle().contains("形势与政策")) {
                                        count = 2;
                                    }else if(geDTO.getTitle()!=null && geDTO.getTitle().contains("毛泽东思想和中国特色社会主义") && geDTO.getTitle().contains("二")){
                                        count = 8;
                                    }
                                    if(count!=0){
                                        term = termRepository.findTermByGradeAndTermCount(major.getGrade(), count);
                                        if(term == null){
                                            term = new Term();
                                            term.setPart(count % 2 == 0 ? 2 : 1);
                                            term.setYear(major.getGrade() + (count-1) / 2);
                                            termRepository.save(term);
                                            term = termRepository.findTermByGradeAndTermCount(major.getGrade(), count);
                                        }
                                        locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, count, cc.getId(), 0);
                                        if (locator == null) {
                                            locator = new Locator();
                                            locator.setTerm(term);
                                            locator.setCourseClass(cc);
                                            locator.setMajor(major);
                                            locatorRepository.save(locator);
                                            locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i + 1, cc.getId(), 0);
                                        }
                                    }
                                    else {
                                        locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i + 1, cc.getId(), 0);
                                        if (locator == null) {
                                            locator = new Locator();
                                            term = termRepository.findTermByGradeAndTermCount(major.getGrade(), i + 1);
                                            if (term == null) {
                                                term = new Term();
                                                if (i == 8) {
                                                    term.setPart(1);
                                                    term.setYear(major.getGrade() + 4);
                                                } else {
                                                    term.setPart((i + 1) % 2 == 0 ? 2 : 1);
                                                    term.setYear(major.getGrade() + i / 2);
                                                }
                                                term.setWeeks(16);
                                                termRepository.save(term);
                                                term = termRepository.findTermByGradeAndTermCount(major.getGrade(), i + 1);
                                            }
                                            locator.setTerm(term);
                                            locator.setCourseClass(cc);
                                            locator.setMajor(major);
                                            locatorRepository.save(locator);
                                            locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i + 1, cc.getId(), 0);
                                        }
                                    }
                                    PlanCourse planCourse = planCourseDao.findByCourseAndLocator(course,locator);
                                    if(planCourse == null) {
                                        planCourse = new PlanCourse();
                                        planCourse.setLocator(locator);
                                        planCourse.setCourse(course);
                                    }
                                    planCourse.setCredits(geDTO.getCredits()[i]);
                                    planCourse.setPeriod(geDTO.getPeriod()[i]);
                                    CourseExam ce = courseExamDao.findOne(geDTO.getCourseExamId());
                                    planCourse.setCourseExam(ce);
                                    if(geDTO.getTitle().contains("*")){
                                        planCourse.setIsDegCourse(1);
                                    }else{
                                        planCourse.setIsDegCourse(0);
                                    }
                                    planCourseDao.save(planCourse);

                                    Imp imp = impRepository.getImpByCourseIdAndLocatorId(course.getId(),locator.getId());
                                    if(imp == null){
                                        imp = new Imp();
                                        imp.setCourse(course);
                                        imp.setLocator(locator);
                                    }
                                    imp.setCredits(geDTO.getCredits()[i]);
                                    imp.setPeriodWeeks(locator.getTerm().getWeeks());
                                    imp.setPeriodHours(geDTO.getPeriod()[i]);
                                    imp.setCourseExam(ce);
                                    if(geDTO.getTitle().contains("*")){
                                        imp.setIsDegCourse(1);
                                    }else{
                                        imp.setIsDegCourse(0);
                                    }
                                    impRepository.save(imp);
                                }

                            }
                        } else  {
                            for (int i = 0; i < 9; i++) {
                                if (geDTO.getPeriod()[i] != 0 || geDTO.getCredits()[i] != 0) {
                                    int ctId = 0;
                                    if(!geDTO.getTitle().isEmpty()){
                                        CourseType ct = courseTypeDao.findByTitleLike(geDTO.getTitle());
                                        if(ct!=null) {
                                            ctId = ct.getId();
                                        }
                                    }
                                    Locator locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i+1, cc.getId(), ctId);
                                    PlanSpec planSpec = planSpecDao.findByLocator(locator);
                                    if(planSpec == null){
                                        planSpec = new PlanSpec();
                                        planSpec.setLocator(locator);
                                    }
                                    planSpec.setPeriod(geDTO.getPeriod()[i]);
                                    planSpec.setCredits(geDTO.getCredits()[i]);
                                    planSpecDao.save(planSpec);
                                }
                            }
                        }

                }
            }
        }
    }

}
