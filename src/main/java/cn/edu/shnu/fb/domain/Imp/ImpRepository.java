package cn.edu.shnu.fb.domain.Imp;

import java.util.ArrayList;
import java.util.List;

import org.h2.table.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseExamDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class ImpRepository {

    @Autowired
    ImpDao impDao;

    @Autowired
    MajorDao majorDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    CourseTypeDao courseTypeDao;

    @Autowired
    CourseClassDao courseClassDao;

    @Autowired
    TermRepository termRepository;

    @Autowired
    LocatorRepository locatorRepository;

    @Autowired
    CourseExamDao courseExamDao;

    @Autowired
    LocatorDao locatorDao;

    @Autowired
    PlanCourseDao planCourseDao;

    public List<Imp> getObligeImpByMajorIdAndTermCount(int majorId,int termCount){
        if(termCount>0 && termCount<9) {
            List<Imp> resList = new ArrayList<>();

            Iterable<CourseClass> courseClasses = courseClassDao.findByTitleNotLike("%选修%");
            Major major = majorDao.findOne(majorId);

            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            for (CourseClass cC : courseClasses) {
                if (major != null && term != null) {
                    List<Locator> locators = locatorDao.findByMajorAndTermAndCourseClass(major,term,cC);
                    if(locators!=null){
                        if(locators.size()!=0){
                            resList.addAll(impDao.findByLocator(locators.get(0)));
                        }
                    }
                }
            }
            return resList;
        }else{
            return null;
        }
    }

    public List<Imp> getElectableImpByMajorIdAndTermCount(int majorId,int termCount){
        if(termCount>0 && termCount<9) {
            List<Imp> resList = new ArrayList<>();

            Iterable<CourseClass> courseClasses = courseClassDao.findByTitleLike("%选修%");
            Major major = majorDao.findOne(majorId);

            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            for (CourseClass cC : courseClasses) {
                if (major != null && term != null) {
                    List<Locator> locators = locatorDao.findByMajorAndTermAndCourseClass(major,term,cC);
                    if(locators!=null){
                        if(locators.size()!=0){
                            resList.addAll(impDao.findByLocator(locators.get(0)));
                        }
                    }
                }
            }
            return resList;
        }else {
            return null;
        }
    }

    public List<Imp> getAllImpByMajorIdAndTermCount(int majorId,int termCount){
        if(termCount>0 && termCount<9) {
            List<Imp> resList = new ArrayList<>();

            Major major = majorDao.findOne(majorId);

            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
                if (major != null && term != null) {
                    List<Locator> locators = locatorDao.findByMajorAndTerm(major, term);
                    for (Locator locator : locators){
                        resList.addAll(impDao.findByLocator(locator));
                    }
                    return resList;
                }
        }
        return null;
    }

    public List<Imp> getImpByLocatorId(int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        if(locator!=null) {
            return impDao.findByLocator(locator);
        } else {
            return null;
        }
    }

    public void updateImpByGridEntityAndLocatorId(GridEntityDTO entity , int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        if(entity!=null && locator != null) {
            locator.setModified(1);
            Course course = courseDao.findOne(entity.getCourseId());
            Imp imp = impDao.findByLocatorAndCourse(locator, course);
            if (imp == null) {
                imp = new Imp();
            }
            imp.setCredits(entity.getCredits()[0]);
            imp.setPeriodHours(entity.getPeriod()[0]);
            imp.setPeriodWeeks(entity.getPeriodWeeks());
            imp.setIsDegCourse(entity.getIsDegCourse());
            imp.setCourse(course);
            imp.setLocator(locator);
            CourseExam courseExam = courseExamDao.findOne(entity.getCourseExamId());
            imp.setCourseExam(courseExam);
            impDao.save(imp);
        }
    }

    public void updateImpByGridEntity(GridEntityDTO entity , Integer termCount){
        if(termCount>0 && termCount<9) {
            if (entity != null) {
                Imp imp = impDao.findOne(entity.getId());
                Locator locator;
                if(imp==null || imp.getCourse().getId()!=entity.getCourseId()) {
                    imp=new Imp();
                    PlanCourse pc = planCourseDao.findOne(entity.getId());
                    locator = pc.getLocator();
                    imp.setCourse(pc.getCourse());
                }else{
                    locator = imp.getLocator();
                }
                    imp.setCredits(entity.getCredits()[0]);
                    imp.setPeriodHours(entity.getPeriod()[0]);
                    imp.setPeriodWeeks(entity.getPeriodWeeks());
                    imp.setIsDegCourse(entity.getIsDegCourse());
                    Integer ctId = 0;
                    if (locator.getCourseType() != null) {
                        ctId = locator.getCourseType().getId();
                    }
                    Locator newLocator = locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(locator.getMajor().getId(), termCount, locator.getCourseClass().getId(), ctId);
                    newLocator.setModified(1);
                    imp.setLocator(newLocator);
                    CourseExam courseExam = courseExamDao.findOne(entity.getCourseExamId());
                    imp.setCourseExam(courseExam);
                    impDao.save(imp);
                }
            }
    }

    public List<Imp> getElectedImp(Integer majorId ,Integer courseClassId ,Integer courseTypeId) {
        Major major = majorDao.findOne(majorId);
        CourseClass courseClass = courseClassDao.findOne(courseClassId);
        if(major!=null && courseClass!=null) {
            if (courseTypeId != 0) {
                CourseType courseType = courseTypeDao.findOne(courseTypeId);
                return impDao.findByLocatorMajorAndLocatorCourseClassAndLocatorCourseType(major, courseClass, courseType);
            } else {
                return impDao.findByLocatorMajorAndLocatorCourseClass(major, courseClass);
            }
        }
        return null;
    }
    public void deleteImp(Imp imp){
        Locator locator = imp.getLocator();
        locator.setModified(1);
        locatorRepository.save(locator);
        impDao.delete(imp);
    }
    public Imp getImpById(Integer id){
        return impDao.findOne(id);
    }

    public Imp getImpByCourseIdAndLocatorId(Integer courseId,Integer locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        Course course = courseDao.findOne(courseId);
        return impDao.findByLocatorAndCourse(locator,course);
    }

    public void save(Imp imp){
        impDao.save(imp);
    }
}
