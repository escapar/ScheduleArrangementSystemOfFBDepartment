package cn.edu.shnu.fb.application;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanRepository;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouziyi on 15/11/25.
 */

@Service

public class CautionService {
    @Autowired
    ExcelService excelService;
    @Autowired
    LocatorRepository locatorRepository;
    @Autowired
    ImpRepository impRepository;
    @Autowired
    PlanRepository planRepository;
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
    CourseDao courseDao;
    @Autowired
    PlanSpecDao planSpecDao;
    @Autowired
    ImpDao impDao;

    public ArrayList<String> getElectableCaution(int majorId,int currentTermCount){
        ArrayList<String> electCaution = new ArrayList<>();
        Major major = majorDao.findOne(majorId);
        for(int termCount = 1 ; termCount< currentTermCount ; termCount++) {
            if (major != null) {
                Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
                if (term != null) {
                    for (CourseType courseType : courseTypeDao.findAll()) {
                        String resRe = getElectableCautionByMajorAndTermAndCourseClassAndCourseType(majorId, termCount, 3, courseType.getId());
                        if (resRe != null)
                            electCaution.add(resRe);
                    }
                    String resFe = getElectableCautionByMajorAndTermAndCourseClassAndCourseType(majorId, termCount, 4, 0);
                    if (resFe != null)
                        electCaution.add(resFe);
                } else {
                    return null;
                }
            }
        }
        return electCaution;
    }

    public String getElectableCautionByMajorAndTermAndCourseClassAndCourseType(int majorId, int termCount , int courseClassId,int courseTypeId){
        Major major = majorDao.findOne(majorId);
        List<Locator> locators=new ArrayList<>();
        float creditsNeeded=0,creditsAchieved=0;
        if(major!=null){
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null){
                CourseClass courseClass = courseClassDao.findOne(courseClassId);
                if(courseClass!=null){
                    CourseType courseType = courseTypeDao.findOne(courseTypeId);
                    if(courseClassId==1||courseClassId==2||courseClassId==4||courseClassId==5)
                    {
                        locators=locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,courseClassId);
                        if(locators!=null)
                        {
                            creditsNeeded=excelService.getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount, courseClassId);
                            creditsAchieved=excelService.getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, courseClassId);
                            if(creditsNeeded!=creditsAchieved)
                                return "第 "+termCount+" 学期"+courseClass.getTitle()+" 培养方案学分为"+creditsNeeded+"，执行计划学分为"+creditsAchieved+"。";
                        }
                    }
                    else {
                        Locator locator=null;
                        locator= locatorDao.findByMajorAndTermAndCourseClassAndCourseType(major, term, courseClass, courseType);
                        if(locator!=null)
                           {
                            PlanSpec ps = planSpecDao.findByLocator(locator);
                            List<Imp> imps = impDao.findByLocator(locator);
                            if(ps!=null){
                                creditsNeeded=ps.getCredits();
                            }
                            for(Imp imp : imps){
                                creditsAchieved += imp.getCredits();
                            }
                            if(creditsNeeded!=creditsAchieved)
                                return "第 "+termCount+" 学期" + courseType.getTitle()+" 培养方案学分为"+creditsNeeded+"，执行计划学分为"+creditsAchieved+"。";

                        }

                    }


                }
            }
        }
        return null;
    }
    public ArrayList<String> getOblidgeCaution(int majorId){
        ArrayList<String>  oblidgeCaution =new ArrayList<>();
        Major major = majorDao.findOne(majorId);
        if(major!=null){
            for(int i=1;i<=8;i++){
                if(getOblidgeCautionByMajorAndTerm(majorId,i)!=null){
                    oblidgeCaution.add(getOblidgeCautionByMajorAndTerm(majorId,i));
                }
            }
            if(oblidgeCaution.size()!=0) return oblidgeCaution;
            else return null;
        }

        return  null;
    }

    public String getOblidgeCautionByMajorAndTerm(int majorId,int termCount){
        Major major = majorDao.findOne(majorId);
        if(major!=null){
            Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(), termCount);
            if(term!=null){
                List<Locator> obligeLocators=locatorRepository.getLocatorObligeByMajorIdAndTermCount(majorId, termCount);
                List<Imp> imps = new ArrayList<>();
                for(Locator oblidgeLocator : obligeLocators){
                    imps.addAll(impRepository.getImpByLocatorId(oblidgeLocator.getId()));
                }
                List<PlanCourse> plans = new ArrayList<>();
                for(Locator oblidgeLocator : obligeLocators) {
                    plans.addAll(planRepository.getPlanCourseByLocatorId(oblidgeLocator.getId()));
                }
                ArrayList<Integer> impCourseIds=new ArrayList<>();
                Map <Integer,String> impComments= new HashMap<>();
                for(Imp imp : imps){
                    impCourseIds.add(imp.getCourse().getId());
                    impComments.put(imp.getId(),imp.getCourseComment());
                }
                ArrayList<Integer> planCourseIds=new ArrayList<>();
                for(PlanCourse plan : plans){
                    planCourseIds.add(plan.getCourse().getId());
                }

                String addCourse="";
                String subCourse="";
                for(Integer impCourseId :impCourseIds){
                    if(!planCourseIds.contains(impCourseId)){
                        Course course=courseDao.findOne(impCourseId);
                        addCourse=addCourse+"《"+course.getTitle()+"》 "+"(备注：";
                        String impComment = impComments.get(impCourseId) == null ? "无" : impComments.get(impCourseId);
                        addCourse+=impComment;
                        addCourse+=")  ";
                    }

                }
                for(Integer planCourseId :planCourseIds){
                    if(!impCourseIds.contains(planCourseId)){
                        Course course=courseDao.findOne(planCourseId);
                        subCourse=subCourse+"《"+course.getTitle()+"》 ";
                    }

                }
                if(!addCourse.isEmpty()&&!subCourse.isEmpty()){ return "第 "+termCount+" 学期"+"执行计划中新增了 "+addCourse +"删减了 "+subCourse+"。";}
                else if(!addCourse.isEmpty()){ return "第 "+termCount+" 学期"+"执行计划中新增了 "+addCourse+"。"; }
                else if(!subCourse.isEmpty()){ return "第 "+termCount+" 学期"+"执行计划中删减了 "+subCourse+"。"; }
            }

        }

        return null;
    }


    }
