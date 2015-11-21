package cn.edu.shnu.fb.domain.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanSpecDao;
import cn.edu.shnu.fb.interfaces.dto.ElectableLocatorDTO;

/**
 * Created by bytenoob on 15/11/8.
 */
@Repository
public class LocatorRepository {
    @Autowired
    LocatorDao locatorDao;
    @Autowired
    TermRepository termRepository;
    @Autowired
    MajorDao majorDao;
    @Autowired
    CourseClassDao courseClassDao;
    @Autowired
    CourseTypeDao courseTypeDao;
    @Autowired
    PlanSpecDao planSpecDao;
    @Autowired
    ImpDao impDao;
    public Locator getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(int majorId, int termCount , int courseClassId,int courseTypeId){
        Major major = majorDao.findOne(majorId);
        Locator locator = null;
        Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
        CourseClass courseClass = courseClassDao.findOne(courseClassId);
        CourseType courseType = courseTypeDao.findOne(courseTypeId);
        if(major!=null && term!=null && courseClass != null) {
            locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major,term,courseClass,courseType);
            if(locator == null){
                if(courseType!=null) {
                    locator = new Locator(major, term, courseClass,courseType);
                }else{
                    locator = new Locator(major, term, courseClass);
                }
                locatorDao.save(locator);
                locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major, term, courseClass, courseType);
            }
        }

        return locator;
    }

    public ElectableLocatorDTO getLocatorDTOByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(int majorId, int termCount , int courseClassId,int courseTypeId){
        Major major = majorDao.findOne(majorId);
        Locator locator = null;
        Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
        CourseClass courseClass = courseClassDao.findOne(courseClassId);
        CourseType courseType = courseTypeDao.findOne(courseTypeId);
        if(major!=null && term!=null && courseClass != null) {
            locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major,term,courseClass,courseType);
            if(locator == null){
                if(courseType!=null) {
                    locator = new Locator(major, term, courseClass,courseType);
                }else{
                    locator = new Locator(major, term, courseClass);
                }
                locatorDao.save(locator);
                locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major, term, courseClass, courseType);
            }
        }
        ElectableLocatorDTO res = new ElectableLocatorDTO(locator);
        PlanSpec ps = planSpecDao.findByLocator(locator);
        List<Imp> imps = impDao.findByLocator(locator);
        if(ps!=null){
            res.setCreditsNeeded(ps.getCredits());
        }
        float creditsAchieved = 0;
        for(Imp imp : imps){
            creditsAchieved += imp.getCredits();
        }
        res.setCreditsAchieved(creditsAchieved);
        return res;
    }

    public List<Locator> getLocatorByMajorIdAndTermCount(int majorId,int termCount){
        Major major = majorDao.findOne(majorId);
        List<Locator> resList = new ArrayList<>();
        if(major!=null) {
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null) {
                resList = locatorDao.findByMajorAndTerm(major, term);
            }
        }
        return resList;
    }

    public List<ElectableLocatorDTO> getLocatorElectableByMajorIdAndTermCount(int majorId,int termCount){
        Major major = majorDao.findOne(majorId);
        List<Locator> locatorsList = new ArrayList<>();
        List<ElectableLocatorDTO> resList = new ArrayList<>();
        if(major!=null) {
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null) {
                List<CourseClass> CCs = courseClassDao.findByTitleLike("%选修%");
                if(CCs.size()>0) {
                    for (CourseClass cc : CCs) {
                        locatorsList.addAll(locatorDao.findByMajorAndTermAndCourseClass(major, term, cc));
                    }
                }
            }
        }
        for(Locator locator : locatorsList){
            if(!(locator.getCourseType()!=null && locator.getCourseType().getTitle().contains("综合素质类课程")) && !(locator.getCourseClass().getTitle().contains("限定选修课") && locator.getCourseType() == null)) {
                ElectableLocatorDTO elDTO = new ElectableLocatorDTO(locator);
                PlanSpec ps = planSpecDao.findByLocator(locator);
                List<Imp> imps = impDao.findByLocator(locator);
                if (ps != null) {
                    elDTO.setCreditsNeeded(ps.getCredits());
                }
                float creditsAchieved = 0;
                for (Imp imp : imps) {
                    creditsAchieved += imp.getCredits();
                }
                elDTO.setCreditsAchieved(creditsAchieved);
                resList.add(elDTO);
            }
        }
        return resList;
    }

    public Locator getLocatorById(int id){
        return locatorDao.findOne(id);
    }

    public Locator getLocatorByMajorIdAndCourseClassIdAndCourseTypeId(int majorId,int courseClassId,int courseTypeId){
        Major major = majorDao.findOne(majorId);
        CourseClass courseClass = courseClassDao.findOne(courseClassId);
        CourseType courseType = courseTypeDao.findOne(courseTypeId);
        if(major!=null && courseClass!=null) {
            return locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major, null, courseClass, courseType);
        }else{
            return null;
        }
    }

    public void save(Locator locator){
        locatorDao.save(locator);
    }

    public void initLocators(int majorId){
        Major major = majorDao.findOne(majorId);
        Iterable<CourseClass> courseClasses = courseClassDao.findAll();
        Iterable<CourseType> courseTypes = courseTypeDao.findAll();
        int termCount;
        for(termCount = 1 ; termCount < 10; termCount++) {
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            for(CourseClass cc : courseClasses){
                if(!cc.getTitle().contains("限定选修")) {
                    Locator locator = new Locator();
                    locator.setMajor(major);
                    locator.setTerm(term);
                    locator.setCourseClass(cc);
                    locator.setModified(0);
                    locatorDao.save(locator);
                }else {
                    for (CourseType ct : courseTypes) {
                        Locator locator = new Locator();
                        locator.setMajor(major);
                        locator.setTerm(term);
                        locator.setCourseClass(cc);
                        locator.setModified(0);
                        locator.setCourseType(ct);
                        locatorDao.save(locator);
                    }
                }
            }
        }

    }
}
