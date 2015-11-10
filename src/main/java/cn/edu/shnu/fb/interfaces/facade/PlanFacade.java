package cn.edu.shnu.fb.interfaces.facade;

/**
 * Created by bytenoob on 15/10/29.
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.plan.PlanRepository;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.infrastructure.persistence.PlanCourseDao;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.infrastructure.persistence.PlanSpecDao;
import cn.edu.shnu.fb.interfaces.assembler.PlanAssembler;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

@RequestMapping("/api")
@RestController
public class PlanFacade {
    @Autowired
    PlanRepository planRepository;

    @Autowired
    LocatorRepository locatorRepository;
    @Autowired
    MajorDao majorDao;

    @Autowired
    PlanCourseDao planCourseDao;

    @Autowired
    PlanSpecDao planSpecDao;

    @ResponseBody
    @RequestMapping(value="/pc/o/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // PlanCourse Grid
    public List<GridEntityDTO> planCourseGridEntityByMajorAndTerm(@PathVariable Integer majorId,@PathVariable int termCount){
        Iterable<PlanCourse> planCourses = planRepository.getObligePlanCoursesByMajorIdAndTermCount(majorId, termCount);
        return PlanAssembler.PlanCourseToGridEntityDTO(planCourses);
    }


    @ResponseBody
    @RequestMapping(value="/ps/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // PlanSpec Grid
    public List<GridEntityDTO> planSpecGridEntityByMajorAndTerm(@PathVariable Integer majorId,@PathVariable int termCount){
        Iterable<PlanSpec> planSpecs = planRepository.getPlanSpecsByMajorIdAndTermCount(majorId, termCount);
        return PlanAssembler.PlanSpecToGridEntityDTO(planSpecs);
    }

    @ResponseBody
    @RequestMapping(value="/ps/l/{locatorId}",method=RequestMethod.GET) // PlanSpec Grid
    public List<PlanSpec> planSpecGridEntityByMajorAndTerm(@PathVariable Integer locatorId){
        return planRepository.getPlanSpecsByLocatorId(locatorId);
    }

    @ResponseBody
    @RequestMapping(value="/ps/m/{majorId}/t/{termCount}",method=RequestMethod.GET)
    public Iterable<PlanSpec> planSpecByMajorAndTerm(@PathVariable Integer majorId,@PathVariable int termCount){
        return planRepository.getPlanSpecsByMajorIdAndTermCount(majorId, termCount);
    }

    @ResponseBody
    @RequestMapping(value="/pc/m/{majorId}/cc/{courseClassId}/ct/{courseTypeId}",method=RequestMethod.GET)
    public List<GridEntityDTO> planSpecByMajorAndCourseClassAndCourseType(@PathVariable Integer majorId,@PathVariable Integer courseClassId,@PathVariable Integer courseTypeId){
        Locator locator = locatorRepository.getLocatorByMajorIdAndCourseClassIdAndCourseTypeId(majorId, courseClassId, courseTypeId);
        if(locator!=null) {
            Iterable<PlanCourse> planCourses = planRepository.getPlanCoursesByLocator(locator);
            if(planCourses!=null) {
                return PlanAssembler.PlanCourseToGridEntityDTO(planCourses);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/pc/l/{locatorId}/grid",method=RequestMethod.GET)
    public Iterable<GridEntityDTO> planSelectableCourseByLocator(@PathVariable Integer locatorId) {
        Locator locator = locatorRepository.getLocatorById(locatorId);
        if(locator!=null) {
            List<PlanCourse> planCourses = planRepository.getPlanCoursesByLocator(locator);
            return PlanAssembler.PlanCourseToGridEntityDTO(planCourses);
        }else{
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/p/m/{majorId}",method=RequestMethod.GET) // for grid only
    public List<GridEntityDTO> detailedPlanByMajor(@PathVariable Integer majorId){
        List<PlanCourse> planCourses = planRepository.getObligePlanCoursesByMajorId(majorId);
        List<PlanSpec> planSpecs = planRepository.getPlanSpecsByMajorId(majorId);
        return PlanAssembler.mergePlanCoursesAndPlanSpecs(planCourses,planSpecs);
    }

}
