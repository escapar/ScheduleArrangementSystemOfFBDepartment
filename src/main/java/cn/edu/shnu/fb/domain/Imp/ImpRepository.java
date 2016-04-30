package cn.edu.shnu.fb.domain.Imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.h2.command.dml.Merge;
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
import cn.edu.shnu.fb.domain.mergedClass.MergedClass;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.CourseClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseExamDao;
import cn.edu.shnu.fb.infrastructure.persistence.CourseTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.ImpCommentDao;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MergedClassDao;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.MergeDTO;
import cn.edu.shnu.fb.interfaces.dto.MergePageEntityDTO;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class ImpRepository {

    @Autowired
    ImpCommentDao impCommentDao;
    @Autowired
    TeacherDao teacherDao;

    @Autowired
    ImpDao impDao;

    @Autowired
    MergedClassDao mergedClassDao;

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

    public List<Imp> getImpByTeacherIdAndTermId(Integer teacherId,Integer termId){
        Teacher teacher = teacherDao.findOne(teacherId);
        Term term = termRepository.findTermById(termId);
        List<Imp> res = new ArrayList<>();
        if(teacher!=null){
            List<Imp> tmp = teacher.getImps();
            for(Imp i : tmp){
                if(i.getLocator().getTerm().getId() == termId){
                    res.add(i);
                }
            }
        }
        return res;
    }
    public List<Imp> findByMergedClass(MergedClass mc){
        return impDao.findByMergedClass(mc);
    }
    public List<Imp> findBySalary(Salary salary){
        return impDao.findBySalary(salary);
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

    public Imp updateImpByGridEntityAndLocatorId(GridEntityDTO entity , int locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        Imp resimp = null;
        if(entity!=null && locator != null) {
            locator.setModified(1);
            Course course = courseDao.findOne(entity.getCourseId());
            if(course == null){
                course = new Course();
                course.setTitle(entity.getTitle());
                course.setCode(entity.getCode());
                course = courseDao.save(course);
            }
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
            int[] teacherIds = entity.getTeacherIds();
            List<Teacher> teacherList = new ArrayList<>();
            for (Integer tId : teacherIds) {
                if(tId!=0) {
                    Teacher teacher = teacherDao.findOne(tId);
                    if (teacher != null) {
                        teacherList.add(teacher);
                    }
                }
            }
            imp.setTeachers(teacherList);
            resimp = impDao.save(imp);
        }
        return resimp;
    }

    public GridEntityDTO updateImpByGridEntity(GridEntityDTO entity , Integer termCount){
        Imp res ;
        PlanCourse pc = null;
        if(termCount>0 && termCount<9) {
            if (entity != null) {
                Imp imp = impDao.findOne(entity.getId());
                Locator locator;
                if(imp==null || imp.getCourse().getId()!=entity.getCourseId()) {
                    imp=new Imp();
                    pc = planCourseDao.findOne(entity.getId());
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
                    int[] teacherIds = entity.getTeacherIds();
                    List<Teacher> teacherList = new ArrayList<>();
                    for (Integer tId : teacherIds) {
                        if(tId!=0) {
                            Teacher teacher = teacherDao.findOne(tId);
                            if (teacher != null) {
                                teacherList.add(teacher);
                            }
                        }
                    }
                    imp.setTeachers(teacherList);

                if(entity.getComment()!=null && !entity.getComment().isEmpty()) {
                        imp.setCourseComment(entity.getComment());
                    }

                res = impDao.save(imp);
                return new GridEntityDTO(res);
                }
            }
        return entity;
    }

    public GridEntityDTO newImpByGridEntity(GridEntityDTO entity , Integer termCount,Major major){
        Imp res ;
        if(termCount>0 && termCount<9) {
            if (entity != null) {
                Imp imp = new Imp();
                CourseClass cc = courseClassDao.findOne(entity.getCourseClassId());
                CourseType ct = courseTypeDao.findOne(entity.getCourseTypeId());
                Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
                Locator locator = locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major, term, cc, ct);
                if(locator == null){
                    locator = new Locator();
                    locator.setCourseClass(cc);
                    locator.setCourseType(ct);
                    locator.setTerm(term);
                    locator.setMajor(major);
                }
                imp.setCourse(courseDao.findOne(entity.getCourseId()));
                imp.setLocator(locator);
                imp.setCredits(entity.getCredits()[0]);
                imp.setPeriodHours(entity.getPeriod()[0]);
                imp.setPeriodWeeks(entity.getPeriodWeeks());
                imp.setIsDegCourse(entity.getIsDegCourse());
                CourseExam courseExam = courseExamDao.findOne(entity.getCourseExamId());
                imp.setCourseExam(courseExam);
                int[] teacherIds = entity.getTeacherIds();
                List<Teacher> teacherList = new ArrayList<>();
                for (Integer tId : teacherIds) {
                    if(tId!=0) {
                        Teacher teacher = teacherDao.findOne(tId);
                        if (teacher != null) {
                            teacherList.add(teacher);
                        }
                    }
                }
                imp.setTeachers(teacherList);

                if(entity.getComment()!=null && !entity.getComment().isEmpty()) {
                    imp.setCourseComment(entity.getComment());
                }

                res = impDao.save(imp);
                return new GridEntityDTO(res);
            }
        }
        return entity;
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

    public void persistImpComment(Integer majorId,Integer termCount,String comment){
        if(termCount>0 && termCount<9 && !comment.isEmpty()) {
            Major major = majorDao.findOne(majorId);
            if(major!=null) {
                Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
                if (term != null) {
                    ImpComment impComment = impCommentDao.findByTermAndMajor(term, major);
                    if (impComment == null) {
                        impComment = new ImpComment();
                        impComment.setMajor(major);
                        impComment.setTerm(term);
                    }
                    impComment.setComment(comment);
                    impCommentDao.save(impComment);
                }
            }
        }
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

    public List<MergePageEntityDTO> getMergedImps(Boolean refuseVerified){
        List<MergePageEntityDTO> res = new ArrayList<>();
        List<Imp> imps = new ArrayList<>();
        Iterable<MergedClass> mcs = mergedClassDao.findAll();
        for(MergedClass mc : mcs){
            if(refuseVerified && mc.getStatus() == 1)
                continue;
            imps = mc.getImps();
            if(imps.size() > 1 ) {
                MergePageEntityDTO mpeDTO = new MergePageEntityDTO(imps.get(0).getTeachers());
                mpeDTO.addImpList(imps);
                res.add(mpeDTO);
            }
        }
        return res;
    }

    public List<MergePageEntityDTO> getImpsForMerge(Term term){
        List<MergePageEntityDTO> res = new ArrayList<>();
        Iterable<Teacher> teachers = teacherDao.findAll();
        for(Teacher T : teachers){
            Iterable<Imp> impT = T.getImps();
            List<Imp> imps = new ArrayList<>();
            for(Iterator<Imp> it=impT.iterator();it.hasNext();){
                Imp imp = it.next();
                if(imp.getMergedClass() == null && imp.getLocator().getTerm().getId() == term.getId()) {
                    imps.add(imp); // ignore all merged Imps
                }
            }
            if(imps.size() <= 1){
                continue;
            }
            Map<Integer , List<Imp>> groupedImp = groupByCategoryType(imps);
            MergePageEntityDTO mpeDTO = new MergePageEntityDTO(T);
            for(Map.Entry<Integer, List<Imp>> entry: groupedImp.entrySet()) {
                List<Imp> impGrouped = entry.getValue();
                if(impGrouped.size()>1) {
                    mpeDTO.addImpList(impGrouped);
                }
            }
            if(mpeDTO.getImps().size()>0) {
                res.add(mpeDTO);
            }
        }
        return res;
    }
    public void undoMergeImps(List<MergeDTO> mergeDTOs){ // designed to remove a set of imps who have safe mergedClass ( in same course )
        MergedClass mergedClass = null;
        for(MergeDTO mergeDTO : mergeDTOs){
            Imp imp = impDao.findOne(mergeDTO.getImpId());
            if(mergedClass == null){
                mergedClass = imp.getMergedClass();
            }
            imp.setMergeComment("");
            imp.setMergedClass(null);
            impDao.save(imp);
        }
        mergedClassDao.delete(mergedClass);
    }
    public Map<Integer, List<Imp>> groupByCategoryType(List<Imp> list) {
        Map<Integer, List<Imp>> map = new TreeMap<Integer, List<Imp>>();
        for (Imp o : list) {
            List<Imp> group = map.get(o.getCourse().getId());
            if (group == null) {
                group = new ArrayList();
                map.put(o.getCourse().getId(), group);
            }
            group.add(o);
        }
        return map;
    }



    public Imp getImpByCourseIdAndLocatorId(Integer courseId,Integer locatorId){
        Locator locator = locatorDao.findOne(locatorId);
        Course course = courseDao.findOne(courseId);
        return impDao.findByLocatorAndCourse(locator, course);
    }

    public void save(Imp imp){
        impDao.save(imp);
    }

    public ImpComment getImpCommentByMajorIdAndTermCount(Integer majorId, Integer termCount){
        Major major = majorDao.findOne(majorId);
        Term term = null;
        if(major!=null){
            term = termRepository.findTermByGradeAndTermCount(major.getGrade(),termCount);
        }
        return impCommentDao.findByTermAndMajor(term,major);
    }

    public void mergeImps(List<MergeDTO> mergeDTOs){
        if(mergeDTOs.size()>1){
            MergedClass mergedClass = new MergedClass();
            int i=0;
            float periodWeeks = 0;
            float periodHours = 0;
            float periodSum = -1;
            List<Imp> imps = new ArrayList<>();
            for(MergeDTO dto : mergeDTOs){
                Imp imp = impDao.findOne(dto.getImpId());
                imp.setMergeComment(dto.getMergeComment());
                imps.add(imp);
                if (imp.getPeriodWeeks() * imp.getPeriodHours() > periodSum ){
                    periodSum = imp.getPeriodWeeks() * imp.getPeriodHours();
                    periodWeeks = imp.getPeriodWeeks();
                    periodHours = imp.getPeriodHours();
                }
                mergedClass.setPeriodHours(periodHours);
                mergedClass.setPeriodWeeks(periodWeeks);
            }
            mergedClass = mergedClassDao.save(mergedClass);
            for(Imp imp : imps){
                imp.setMergedClass(mergedClass);
                impDao.save(imp);
            }
        }
    }

    public List<MergePageEntityDTO> getImpsMergeForVerify(){
        List<MergePageEntityDTO> res = new ArrayList<>();
        Iterable<Teacher> teachers = teacherDao.findAll();
        for(Teacher T : teachers){
            Iterable<Imp> impT = T.getImps();
            List<Imp> imps = new ArrayList<>();
            for(Iterator<Imp> it=impT.iterator();it.hasNext();){
                Imp imp = it.next();
                if(imp.getMergedClass() != null && imp.getMergedClass().getStatus()!=1) {
                    imps.add(imp); // ignore all merged Imps
                }
            }
            if(imps.size() <= 1){
                continue;
            }
            Map<Integer , List<Imp>> groupedImp = groupByCategoryType(imps);
            MergePageEntityDTO mpeDTO = new MergePageEntityDTO(T);
            for(Map.Entry<Integer, List<Imp>> entry: groupedImp.entrySet()) {
                List<Imp> impGrouped = entry.getValue();
                if(impGrouped.size()>1) {
                    mpeDTO.addImpList(impGrouped);
                }
            }
            if(mpeDTO.getImps().size()>0) {
                res.add(mpeDTO);
            }
        }
        return res;
    }

    public void verifyMergeImps(List<MergeDTO> mergeDTOs){
        if(mergeDTOs.size()>1){
            for(MergeDTO dto : mergeDTOs){
                Imp imp = impDao.findOne(dto.getImpId());
                MergedClass mc = imp.getMergedClass();
                mc.setStatus(1);
                mergedClassDao.save(mc);
            }
        }
    }

    public List<Imp> getRejectedImps(Term term){
        return impDao.findBySalaryRejectedAndLocatorTerm(1,term);
    }
}
