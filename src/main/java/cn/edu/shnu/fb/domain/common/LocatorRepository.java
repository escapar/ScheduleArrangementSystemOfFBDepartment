package cn.edu.shnu.fb.domain.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;

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
                locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major,term,courseClass,courseType);
            }
        }
        return locator;
    }

    public List<Locator> getLocatorByMajorIdAndTermCount(int majorId,int termCount){
        Major major = majorDao.findOne(majorId);
        List<Locator> resList = new ArrayList<>();
        if(major!=null) {
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null) {
                resList = locatorDao.findByMajorAndTerm(major,term);
            }
        }
        return resList;
    }

    public List<Locator> getLocatorElectableByMajorIdAndTermCount(int majorId,int termCount){
        Major major = majorDao.findOne(majorId);
        List<Locator> resList = new ArrayList<>();
        if(major!=null) {
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null) {
                List<CourseClass> CCs = courseClassDao.findByTitleNotLike("%必修%");
                if(CCs.size()>0) {
                    for (CourseClass cc : CCs) {
                        resList.addAll(locatorDao.findByMajorAndTermAndCourseClass(major, term, cc));
                    }
                }
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
}
