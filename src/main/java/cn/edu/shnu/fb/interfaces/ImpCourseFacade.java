package cn.edu.shnu.fb.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @ResponseBody
    @RequestMapping(value="/imp/major/{grade}/{title}/term/{termCount}/oblige",method=RequestMethod.GET)
    public List<PlanCourseGridDTO> impDetailByTermAndMajor(@PathVariable Integer grade,@PathVariable String title,@PathVariable Integer termCount){
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
        Imp imp = impRepository.findImpByMajorAndTerm(IC.getImp().getMajor(),term);
        if(imp!=null){
            IC.setImp(imp);
            impCourseRepository.save(IC);
        }
    }
}
