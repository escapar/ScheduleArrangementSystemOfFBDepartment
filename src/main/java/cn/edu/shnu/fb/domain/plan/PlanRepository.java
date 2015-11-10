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
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanSpecDao;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class PlanRepository {

    @Autowired
    PlanCourseDao planCourseDao;

    @Autowired
    PlanSpecDao planSpecDao;

    @Autowired
    MajorDao majorDao;

    @Autowired
    LocatorDao locatorDao;

    @Autowired
    CourseClassDao courseClassDao;
    @Autowired
    TermRepository termRepository;

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
        Iterable<CourseClass> courseClasses = courseClassDao.findByTitleLike("%必修%");
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
            Iterable<CourseClass> courseClasses = courseClassDao.findByTitleLike("%必修%");
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
}
