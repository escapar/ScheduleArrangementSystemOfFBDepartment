package cn.edu.shnu.fb.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseClassRepository;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.common.CourseExamRepository;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.CourseTypeRepository;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.course.CourseRepository;
import cn.edu.shnu.fb.domain.imp.Imp;
import cn.edu.shnu.fb.domain.imp.ImpCourse;
import cn.edu.shnu.fb.domain.imp.ImpCourseRepository;
import cn.edu.shnu.fb.domain.imp.ImpRepository;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.major.MajorRepository;
import cn.edu.shnu.fb.domain.plan.Plan;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;

/**
 * Created by bytenoob on 15/11/2.
 */


@RequestMapping("/api")
@RestController
public class ImpCourseFacade {
    @Autowired
    ImpCourseRepository impCourseRepository;

    @Autowired
    TermRepository termRepository;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    ImpRepository impRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseClassRepository courseClassRepository;

    @Autowired
    CourseExamRepository courseExamRepository;

    @Autowired
    CourseTypeRepository courseTypeRepository;
    @ResponseBody
    @RequestMapping(value="/imp/major/{grade}/{title}/term/{termCount}/oblige",method=RequestMethod.GET)
    public List<PlanCourseGridDTO> impDetailByTermAndMajor(@PathVariable Integer grade,@PathVariable String title,@PathVariable Integer termCount){
        System.out.println(title);
        int termYear = grade + (termCount - 1) / 2 ;
        int termPart = termCount % 2;
        Term term = termRepository.findTermByYearAndPart(termYear,termPart);
        Major major = majorRepository.findMajorByGradeAndTitleLike(grade, title);
        Iterable<ImpCourse> impCourses = impCourseRepository.findImpCoursesByImpTermAndImpMajor(term, major);
        List<PlanCourseGridDTO> PCGDTOs = new ArrayList<>();
        for(ImpCourse ICtmp : impCourses){
            if(ICtmp.getCourseClass().getTitle().equals("公共必修课") || ICtmp.getCourseClass().getTitle().equals("专业必修课"))
                PCGDTOs.add(new PlanCourseGridDTO(new ImpCourseDTO(ICtmp)));
        }
        return PCGDTOs;
    }

    @ResponseBody
    @RequestMapping(value="/imp/major/{grade}/{title}/term/{termCount}/selectable",method=RequestMethod.GET)
    public List<PlanCourseGridDTO> impSelectableDetailByTermAndMajor(@PathVariable Integer grade,@PathVariable String title,@PathVariable Integer termCount){
        System.out.println(title);
        int termYear = grade + (termCount - 1) / 2 ;
        int termPart = termCount % 2;
        Term term = termRepository.findTermByYearAndPart(termYear,termPart);
        Major major = majorRepository.findMajorByGradeAndTitleLike(grade, title);
        Iterable<ImpCourse> impCourses = impCourseRepository.findImpCoursesByImpTermAndImpMajor(term, major);
        List<PlanCourseGridDTO> PCGDTOs = new ArrayList<>();
        for(ImpCourse ICtmp : impCourses){
            if(!(ICtmp.getCourseClass().getTitle().equals("公共必修课") || ICtmp.getCourseClass().getTitle().equals("专业必修课")))
                PCGDTOs.add(new PlanCourseGridDTO(new ImpCourseDTO(ICtmp)));
        }
        return PCGDTOs;
    }

    @ResponseBody
    @RequestMapping(value="/imp/{id}",method=RequestMethod.GET)
    public ImpCourseDTO byID(@PathVariable Integer id){
        ImpCourse ICtmp = impCourseRepository.findOne(id);
        if(ICtmp != null) {
            return new ImpCourseDTO(ICtmp);
        }else {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/imp/{id}/term/{year}/{part}",method=RequestMethod.POST)
    public void updateTerm(@PathVariable Integer id,@PathVariable Integer year,@PathVariable Integer part){
        ImpCourse IC = impCourseRepository.findOne(id);
        Term term = termRepository.findTermByYearAndPart(year, part);
        Imp imp = impRepository.findImpByMajorAndTerm(IC.getImp().getMajor(), term);
        if(imp!=null){
            IC.setImp(imp);
            impCourseRepository.save(IC);
        }
    }

    @ResponseBody
    @RequestMapping(value="/plan/major/{majorGrade}/{majorTitle}/term/{termCount}/class/{courseClass}/type/{courseType}",method=RequestMethod.POST , consumes = "application/json")
    public void updateSelectableImpCourse(@RequestBody List<PlanCourseGridDTO> courseGridDTOs,@PathVariable Integer majorGrade,@PathVariable String majorTitle,@PathVariable Integer termCount,@PathVariable Integer courseClass,@PathVariable Integer courseType){
                System.out.println(majorTitle);

        Major major = majorRepository.findMajorByGradeAndTitleLike(majorGrade, majorTitle);
        int termYear = major.getGrade() + (termCount - 1) / 2 ;
        int termPart = termCount % 2;
        Term term = termRepository.findTermByYearAndPart(termYear, termPart);
        //add
        for(PlanCourseGridDTO PCGDTO : courseGridDTOs) {
            Imp imp = impRepository.findImpByMajorAndTerm(major, term);
            Course course = courseRepository.findCourseByTitle(PCGDTO.getTitle());
            ImpCourse icTmp = impCourseRepository.findByCourse(course);
            if(icTmp == null) {
                icTmp = new ImpCourse();
            }
                icTmp.setCredits(PCGDTO.getCredits()[0]);
                icTmp.setPeriodHPerWeek(PCGDTO.getPeriod()[0]);
                icTmp.setPeriodInWeek(16);
                icTmp.setImp(imp);
                icTmp.setCourse(course);
                CourseClass courseClassNew = courseClassRepository.findOne(courseClass);
                icTmp.setCourseClass(courseClassNew);
                if(courseType != 0) {
                    CourseType courseTypeNew = courseTypeRepository.findOne(courseType);
                    icTmp.setCourseType(courseTypeNew);
                }
                //TODO!
                icTmp.setIsDegCourse(0);
                CourseExam courseExam = new CourseExam();
                courseExam.setId(1);
                courseExam.setTitle("考试");
                icTmp.setCourseExam(courseExam);
                impCourseRepository.save(icTmp);
        }



        List<ImpCourse> crrICs = impCourseRepository.findImpCoursesByImpTermAndImpMajor(term, major);
        //delete
        Boolean needToDelete = true;
        for(ImpCourse crrIC :crrICs){
            for(PlanCourseGridDTO PCGDTO : courseGridDTOs) {
                if(!(crrIC.getCourseClass().getId() == courseClass  && ((courseType == 0 && crrIC.getCourseType()== null) || (crrIC.getCourseType()!= null && crrIC.getCourseType().getId() == courseType)))) {
                    needToDelete = false;

                }
                else if(crrIC.getCourse().getTitle().equals(PCGDTO.getTitle())) {
                    needToDelete = false;
                }

                        //标题相同 类别相同 不删
                        //标题相同 类别不同 不删
                        //标题不同 类别不同 不删
                        //标题不同 类别相同 删
            }
            if(needToDelete) {
                impCourseRepository.delete(crrIC);
            }
            needToDelete=true;
        }


    }
}
