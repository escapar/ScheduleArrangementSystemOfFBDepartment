package cn.edu.shnu.fb.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.domain.user.User;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;
import cn.edu.shnu.fb.infrastructure.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpComment;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanRepository;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.infrastructure.poi.ExcelTemplate;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;
import cn.edu.shnu.fb.infrastructure.persistence.ImpCommentDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.interfaces.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bytenoob on 15/11/10.
 */
@Service


public class ExcelService {
    @Autowired
    ImpRepository impRepository;

    @Autowired
    TermRepository termRepository;

    @Autowired
    LocatorRepository locatorRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    MajorDao majorDao;

    @Autowired
    ImpCommentDao impCommentDao;

    @Autowired
    TeacherDao teacherDao;

    @Autowired
    UserDao userDao;

    public List<SalaryDTO> generateSalaryDTOs(InputStream is , int type){
        // type : 0 文修 1 副修 2 研究生
        ExcelTemplate template = ExcelTemplate.newInstance(is);
        ArrayList<String> excelTeacherNames=template.getTeacherNames();
        ArrayList<String> excelTeacherIdCodes=template.getTeacherIdCodes();
        Iterable<Teacher> dbTeachers = teacherDao.findAll();
        ArrayList<String> dbTeacherNames=new ArrayList<>();
        ArrayList<Teacher> teachers=new ArrayList<>();
        for(Teacher dbTeacher : dbTeachers){
            dbTeacherNames.add(dbTeacher.getName());
        }
        for(String excelTeacherName : excelTeacherNames){
            if(dbTeacherNames.contains(excelTeacherName)){
                teachers.add(teacherDao.findByName(excelTeacherName));
            }
            else{
                Teacher newTeacher=new Teacher();
                newTeacher.setName(excelTeacherName);
                int index=excelTeacherNames.indexOf(excelTeacherName);
                newTeacher.setIdCode(excelTeacherIdCodes.get(index));
                newTeacher.setDepartment("");
                newTeacher.setProTitle("");
                newTeacher.setType("");
                teacherDao.save(newTeacher);
                User user = userDao.findByTeacher(newTeacher);
                if(user == null) user = new User();
                user.setRole(1);
                user.setTeacher(newTeacher);
                if(newTeacher.getIdCode().isEmpty()||newTeacher.getIdCode()==null)
                {user.setUsername(newTeacher.getName());}
                else {user.setUsername(newTeacher.getIdCode());}
                userDao.save(user);
                dbTeacherNames.add(excelTeacherName);
                teachers.add(newTeacher);
            }
        }

        return template.getSalaryDTOs(type,teachers);

    }

    public ImpExcelDTO generateImpExcelDTO(int  majorId, int termCount){

        float  planpoc, planmoc ,planrec, planfec, plantc,planSumC=0 ;
        float  imppoc, impmoc, imprec, impfec,imptc,impSumC=0;
        //根据majoid,termcount,courseclassid找出公共必修课的imps和plans

        List<ImpExcelGridDTO> POLists = new ArrayList<>();
        List<Locator> polocators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,1);
        List<Imp> poimps = new ArrayList<>();
        for(Locator polocator : polocators){
            poimps.addAll(impRepository.getImpByLocatorId(polocator.getId()));
        }
        for(Imp poimp : poimps){
            POLists.add(toExcelDTO(poimp));
        }
        //计算imp和plan的公共必修课的总学分
        imppoc=getImpCreditsByMajorAndTermAndCourseClass(majorId,termCount,1);
        planpoc=getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount, 1);
        //根据majoid,termcount,courseclassid找出专业必修课的imps

        List<ImpExcelGridDTO> MOLists = new ArrayList<>();
        List<Locator> molocators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,2);
        List<Imp> moimps = new ArrayList<>();
        for(Locator molocator : molocators){
            moimps.addAll(impRepository.getImpByLocatorId(molocator.getId()));
        }
        for(Imp moimp : moimps){
            MOLists.add(toExcelDTO(moimp));
        }
        //计算imp和plan的专业必修课的总学分
        impmoc=getImpCreditsByMajorAndTermAndCourseClass(majorId,termCount,2);
        planmoc=getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount, 2);
        //根据majoid,termcount,courseclassid找出限定选修课的imps

        List<ImpExcelGridDTO> RELists = new ArrayList<>();
        List<Locator> relocators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,3);
        List<Imp> reimps = new ArrayList<>();
        for(Locator relocator : relocators){
            reimps.addAll(impRepository.getImpByLocatorId(relocator.getId()));
        }

        for(Imp reimp : reimps){
            RELists.add(toExcelDTO(reimp));
        }
        //计算imp和plan的限定选修课的总学分
        imprec=getImpCreditsByMajorAndTermAndCourseClass(majorId,termCount,3);
        planrec=getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount, 3);
        //根据majoid,termcount,courseclassid找出任意选修课的imps和plans

        List<ImpExcelGridDTO> FELists = new ArrayList<>();
        List<Locator> felocators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,4);
        List<Imp> feimps = new ArrayList<>();
        for(Locator felocator : felocators){
            feimps.addAll(impRepository.getImpByLocatorId(felocator.getId()));
        }
        for(Imp feimp : feimps){
            FELists.add(toExcelDTO(feimp));
        }
        //计算imp和plan的任意选修课的总学分
        impfec=getImpCreditsByMajorAndTermAndCourseClass(majorId,termCount,4);
        planfec=getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount, 4);

        //根据majoid,termcount,courseclassid找出实践性环节的imps和plans
        List<ImpExcelGridDTO> TLists = new ArrayList<>();
        List<Locator> tlocators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,5);
        List<Imp> timps = new ArrayList<>();
        for(Locator tlocator : tlocators){
            timps.addAll(impRepository.getImpByLocatorId(tlocator.getId()));
        }
        for(Imp timp : timps){
            TLists.add(toExcelDTO(timp));
        }
        //计算imp和plan的实践性环节的总学分
        imptc=getImpCreditsByMajorAndTermAndCourseClass(majorId,termCount,5);
        plantc=getPlanCreditsByMajorAndTermAndCourseClass(majorId, termCount,5);
        //获取majorCode
        String majorCode="";
        Major major=majorDao.findOne(majorId);
        majorCode=major.getMajorType().getMajorCode();
        String majorTitle=major.getMajorType().getTitle();

        //获取impComment
        String impComment="";
        Term term = termRepository.findTermByGradeAndTermCount(major.getGrade(),termCount);
        ImpComment impcom = impCommentDao.findImpCommentByMajorIdAndTermId(majorId, term.getId());
        if(impcom!=null)
            impComment=impcom.getComment();

        ImpExcelDTO res = new ImpExcelDTO(new MajAndTmAndImpComDTO(majorTitle,majorCode,termCount,impComment),new ImpExcelHeaderDTO(planpoc,planmoc,planrec,planfec,plantc,imppoc,impmoc,imprec,impfec,imptc),POLists,MOLists,RELists,FELists,TLists);
        return res;
    }

    public List<GridEntityDTO> generatePlan(InputStream is){
        ExcelTemplate template = ExcelTemplate.newInstance(is);
        return template.getCourseGridEntity();
    }

    public ImpCreditsDTO generateImpCreditsDTO(int majorId){

        CreditsDTO fircredto=getCreditDTOByMajorAndTerm(majorId, 1);
        CreditsDTO seccredto=getCreditDTOByMajorAndTerm(majorId, 2);
        CreditsDTO thicredto=getCreditDTOByMajorAndTerm(majorId, 3);
        CreditsDTO foucredto=getCreditDTOByMajorAndTerm(majorId,4);
        CreditsDTO fivcredto=getCreditDTOByMajorAndTerm(majorId,5);
        CreditsDTO sixcredto=getCreditDTOByMajorAndTerm(majorId,6);
        CreditsDTO sevcredto=getCreditDTOByMajorAndTerm(majorId,7);
        CreditsDTO eigcredto=getCreditDTOByMajorAndTerm(majorId,8);

        int grade;
        String code;
        String title;
        Major major=majorDao.findOne(majorId);
        grade=major.getGrade();
        code=major.getMajorType().getMajorCode();
        title=major.getMajorType().getTitle();

        SumCreditsDTO sumCreditsdto=getSumCreditsDTOByMajor(majorId);
        ImpCreditsDTO res=new ImpCreditsDTO(new CreditsHeaderDTO(grade,code,title),fircredto,seccredto,thicredto,foucredto,fivcredto,sixcredto,sevcredto,eigcredto,sumCreditsdto);
        return res;
    }

    public ImpExcelGridDTO toExcelDTO(Imp imp){
        Course course = imp.getCourse();
        CourseExam courseExam = imp.getCourseExam();
        List<Teacher> teachers = imp.getTeachers();

        String courseCode = "";
        String courseTitle = "";
        String courseExamTitle = "";
        String isDegCourseTitle = "";
        List<String> teacherCode = new ArrayList<>();
        List<String> teacherName = new ArrayList<>();
        List<String> teacherTitle = new ArrayList<>();
        String courseComment="";

        if(imp.getCourseComment()!=null){
            courseComment=imp.getCourseComment();}

        if(teachers!=null){
            for(Teacher teacher : teachers){
                teacherCode.add(teacher.getIdCode());
                teacherName.add(teacher.getName());
                teacherTitle.add(teacher.getProTitle());
            }
        }
        if(course != null){
            courseCode = imp.getCourse().getCode();
            courseTitle = imp.getCourse().getTitle();
        }

        if(courseExam != null){
            courseExamTitle = imp.getCourseExam().getTitle();
        }

        if(imp.getIsDegCourse() == 1){
            isDegCourseTitle = "是";
        } else {
            isDegCourseTitle = "否";
        }
        return new ImpExcelGridDTO(courseCode,courseTitle,courseExamTitle,imp.getCredits(),imp.getPeriodWeeks(),
                imp.getPeriodHours(),isDegCourseTitle,teacherCode,teacherName,teacherTitle,courseComment);
    }


    public float getSumCreditsByMajorIdAndCourseClassId(int majorId,int courseclassId){
        float sumcredits=0;

        for(int i=1;i<=8;i++){
            sumcredits=sumcredits+getImpCreditsByMajorAndTermAndCourseClass(majorId, i, courseclassId);
        }

        return sumcredits;
    }


    public float getDebugSumCreditsByMajorIdAndCourseClassId(int majorId,int courseclassId){
        float debugsumcredits=0;
        for(int i=1;i<=8;i++){
            debugsumcredits=debugsumcredits+getImpDebugCreditsByMajorAndTermAndCourseClass(majorId, i, courseclassId);
        }

        return debugsumcredits;
    }

    public CreditsDTO getCreditDTOByMajorAndTerm(int majorId,int termCount){

        String[] credits={"","","","","","","",""};
        float  poc, moc,rec , fec ,tc;
        poc=getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, 1);
        if(poc!=0){credits[0]=String.valueOf((int)poc);}
        moc=getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, 2);
        if(moc!=0){credits[1]=String.valueOf((int)moc);}
        rec=getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, 3);
        if(rec != 0){credits[2]=String.valueOf((int)rec);}
        fec=getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, 4);
        if(fec!=0){credits[5]=String.valueOf((int)fec);}
        tc=getImpCreditsByMajorAndTermAndCourseClass(majorId, termCount, 5);
        if(tc!=0){
            if((int)tc==tc) credits[6]=String.valueOf((int)tc);
            else credits[6]=String.valueOf(tc);}


        String[] icredits={"","","","","","","",""};
        float  ipoc,imoc,irec, ifec ,itc;

        ipoc=getImpDebugCreditsByMajorAndTermAndCourseClass(majorId,termCount,1);
        if(ipoc!=0){icredits[0]=String.valueOf((int)ipoc);}
        imoc=getImpDebugCreditsByMajorAndTermAndCourseClass(majorId,termCount,2);
        if(imoc!=0){icredits[1]=String.valueOf((int)imoc);}
        irec=getImpDebugCreditsByMajorAndTermAndCourseClass(majorId,termCount,3);
        if(irec!=0){icredits[2]=String.valueOf((int)irec);}
        ifec=getImpDebugCreditsByMajorAndTermAndCourseClass(majorId,termCount,4);
        if(ifec!=0){icredits[5]=String.valueOf((int)ifec);}
        itc=getImpDebugCreditsByMajorAndTermAndCourseClass(majorId,termCount,5);
        if(itc!=0){if((int)itc==itc) icredits[6]=String.valueOf((int)itc);
        else icredits[6]=String.valueOf(itc);}

        float litcsum=getLitCreditsByMajorAndTerm(majorId,termCount);
        if(litcsum!=0){credits[3]=String.valueOf((int)litcsum);}  //文修核心类课程没有学位课
        if(termCount==8){credits[4]=String.valueOf(2);credits[7]=String.valueOf(6);}  //讲座和毕业论文也是非学位课

        float fsum=0,fisum=0;
        fsum=poc+moc+rec+fec+tc+litcsum;
        fisum=ipoc+imoc+irec+ifec+itc;
        if(termCount==8){fsum=fsum+8;}

        String sum="0",isum="0";
        if(fsum!=0){
            if((int)fsum==fsum) sum=String.valueOf((int)fsum);
            else sum=String.valueOf(fsum);
        }

        if(fisum!=0){
            if((int)fisum==fisum) isum=String.valueOf((int)fisum);
            else isum=String.valueOf(fisum);
        }

        return new CreditsDTO(credits[0],credits[1],credits[2],credits[3],credits[4],credits[5],credits[6],credits[7],icredits[0],icredits[1],icredits[2],icredits[3],icredits[4],icredits[5],icredits[6],icredits[7],sum,isum);
    }

    public float getLitCreditsByMajorAndTerm(int majorId,int termCount){
        List<Locator> litlocators=locatorRepository.getLocatorLitByMajorIdAndTermCount(majorId, termCount);
        List<PlanSpec> planspecs = new ArrayList<>();
        for(Locator litlocator :litlocators){

            planspecs.add(planRepository.getPlanSpecsByLocatorId(litlocator.getId()));
        }

        float litcsum=0;


        for(PlanSpec planspec : planspecs){
            if(planspec!=null) {
                litcsum = litcsum + planspec.getCredits();
            }
        }
        if(termCount==8){litcsum=0;}
        return litcsum;

    }

    public float getSumLitCredits(int majorId){
        float sumlitcredits=0;
        for(int i=1;i<=8;i++){
            sumlitcredits=sumlitcredits+getLitCreditsByMajorAndTerm(majorId,i);
        }
        return sumlitcredits;
    }

    public SumCreditsDTO getSumCreditsDTOByMajor(int majorId){

        String[] sumcredits=new String[]{"0","0","0","0","0","0","0","0"};
        String[] isumcredits=new String[]{"0","0","0","0","0","0","0","0"};

        sumcredits[0]=String.valueOf((int)getSumCreditsByMajorIdAndCourseClassId(majorId, 1));
        sumcredits[1]=String.valueOf((int)getSumCreditsByMajorIdAndCourseClassId(majorId, 2));
        sumcredits[2]=String.valueOf((int)getSumCreditsByMajorIdAndCourseClassId(majorId, 3));
        sumcredits[5]=String.valueOf((int)getSumCreditsByMajorIdAndCourseClassId(majorId, 4));
        float tsum=getSumCreditsByMajorIdAndCourseClassId(majorId, 5);
        if((int)tsum==tsum) sumcredits[6]=String.valueOf((int)getSumCreditsByMajorIdAndCourseClassId(majorId, 5));
        else  sumcredits[6]=String.valueOf(getSumCreditsByMajorIdAndCourseClassId(majorId, 5));
        sumcredits[3]=String.valueOf((int)getSumLitCredits(majorId));
        sumcredits[4]=String.valueOf(2);
        sumcredits[7]=String.valueOf(6);

        isumcredits[0]=String.valueOf((int)getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 1));
        isumcredits[1]=String.valueOf((int)getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 2));
        isumcredits[2]=String.valueOf((int)getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 3));
        isumcredits[5]=String.valueOf((int)getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 4));
        float tisum=getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 5);
        if((int)tisum==tisum) isumcredits[6]=String.valueOf((int)getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 5));
        else  isumcredits[6]=String.valueOf(getDebugSumCreditsByMajorIdAndCourseClassId(majorId, 5));

        float fasum=0,fbsum=0;
        for(int i=0;i<8;i++){
            fasum=fasum+Float.parseFloat(sumcredits[i]);
        }
        for(int i=0;i<8;i++){
            fbsum=fbsum+Float.parseFloat(isumcredits[i]);
        }

        String asum="0",bsum="0";
        if(fasum!=0){
            if((int)fasum==fasum) asum=String.valueOf((int)fasum);
            else asum=String.valueOf(fasum);
        }

        if(fbsum!=0){
            if((int)fbsum==fbsum) bsum=String.valueOf((int)fbsum);
            else bsum=String.valueOf(fbsum);
        }

        return new SumCreditsDTO(sumcredits[0],sumcredits[1],sumcredits[2],sumcredits[3],sumcredits[4],sumcredits[5],sumcredits[6],sumcredits[7],
                isumcredits[0],isumcredits[1],isumcredits[2],isumcredits[3],isumcredits[4],isumcredits[5],isumcredits[6],isumcredits[7],asum,bsum);
    }


    public float getImpCreditsByMajorAndTermAndCourseClass(int majorId,int termCount,int courseClassId){
        float impcredits=0;
        List<Locator> locators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,courseClassId);
        List<Imp> imps = new ArrayList<>();
        for(Locator locator : locators){
            imps.addAll(impRepository.getImpByLocatorId(locator.getId()));
        }
        for(Imp imp : imps){
            impcredits=impcredits+imp.getCredits();
        }
        return impcredits;
    }

    public float getImpHoursByMajorAndTermAndCourseClass(int majorId,int termCount,int courseClassId){
        float imphours=0;
        List<Locator> locators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,courseClassId);
        List<Imp> imps = new ArrayList<>();
        for(Locator locator : locators){
            imps.addAll(impRepository.getImpByLocatorId(locator.getId()));
        }
        for(Imp imp : imps){
            imphours=imphours+imp.getPeriodHours();
        }
        return imphours;
    }
    public float getImpDebugCreditsByMajorAndTermAndCourseClass(int majorId,int termCount,int courseClassId){
        float impdebugcredits=0;
        List<Locator> locators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,courseClassId);
        List<Imp> imps = new ArrayList<>();
        for(Locator locator : locators) {
            imps.addAll(impRepository.getImpByLocatorId(locator.getId()));
        }
        for(Imp imp : imps){
            if(imp.getIsDegCourse()==1){

                impdebugcredits=impdebugcredits+imp.getCredits();
            }
        }
        return impdebugcredits;
    }

    public float getPlanCreditsByMajorAndTermAndCourseClass(int majorId,int termCount,int courseClassId){
        float plancredits=0;
        List<Locator> locators= locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassId(majorId,termCount,courseClassId);
        List<PlanCourse> plans = new ArrayList<>();
        List<PlanSpec> planspecs = new ArrayList<>();
        for(Locator locator : locators){
            plans.addAll(planRepository.getPlanCourseByLocatorId(locator.getId()));
            PlanSpec tmp = planRepository.getPlanSpecByLocator(locator);
            if(tmp!=null)
                planspecs.add(tmp);
        }
        for(PlanCourse plan : plans){

            plancredits=plancredits+plan.getCredits();
        }
        if(plancredits <= 0){
            for(PlanSpec planspec : planspecs){
                plancredits=plancredits+planspec.getCredits();
            }
        }
        return plancredits;
    }

}
