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
import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseExamDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanSpecDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class PlanRepository {

    @Autowired
    PlanCourseDao planCourseDao;

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
    public List<PlanCourse> getPlanCoursesByLocator(Locator locator){
        return planCourseDao.findByLocator(locator);
    }

    public Iterable<PlanSpec> getPlanSpecByLocator(Locator locator){
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
            Iterable<CourseClass> courseClasses = courseClassDao.findByTitleLike("%选修%");
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

    public List<PlanSpec> getPlanSpecsByLocatorId(int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        if(locator!=null){
            return planSpecDao.findByLocator(locator);
        }else{
            return null;
        }
    }

    public void updatePlansByExcelAndMajorId(int majorId,List<GridEntityDTO> geDTOs) {
        Major major = majorDao.findOne(majorId);
        if (major != null) {
            for (GridEntityDTO geDTO : geDTOs) {
                if(geDTO.getCourseClass().contains("总计")){
                    break;
                }
                String title = geDTO.getTitle().replace("*","");
                    List<CourseClass> ccs = courseClassDao.findByTitleLike(geDTO.getCourseClass());

                    if (ccs.size() > 0) {
                        CourseClass cc = ccs.get(0);
                        if (!geDTO.getCourseClass().contains("选修")) {
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
                                    Locator locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i+1, cc.getId(), 0);
                                    if (locator == null) {
                                        locator = new Locator();
                                        Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), i+1);
                                        if(term == null){
                                            term = new Term();
                                            if(i==8) {
                                                term.setPart(1);
                                                term.setYear(major.getGrade() + 4);
                                            }else{
                                                term.setPart(i % 2 == 0 ? 2 : 1);
                                                term.setYear(major.getGrade() + i / 2);
                                            }
                                            term.setWeeks(16);
                                            termRepository.save(term);
                                            term = termRepository.findTermByGradeAndTermCount(major.getGrade(), i+1);
                                        }
                                        locator.setTerm(term);
                                        locator.setCourseClass(cc);
                                        locator.setMajor(major);
                                        locatorRepository.save(locator);
                                        locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i+1, cc.getId(), 0);
                                    }
                                    PlanCourse planCourse = new PlanCourse();
                                    planCourse.setLocator(locator);
                                    planCourse.setCourse(course);
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
                                    Locator locator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, i, cc.getId(), ctId);
                                    PlanSpec planSpec = new PlanSpec();
                                    planSpec.setLocator(locator);
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
