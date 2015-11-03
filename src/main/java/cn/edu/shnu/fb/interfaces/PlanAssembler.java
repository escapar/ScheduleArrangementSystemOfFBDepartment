package cn.edu.shnu.fb.interfaces;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/10/31.
 */
public class PlanAssembler {
    public static final List<PlanCourseGridDTO> mergePlanCoursesAndCreateGridDTO(List<PlanCourseDTO> PCDTOs,Major major) {
        List<PlanCourseGridDTO> PCGDTOs = new ArrayList();
        boolean existsCourse = false;
        for (PlanCourseDTO PCDTO : PCDTOs) {
            for(PlanCourseGridDTO PCGDTO :PCGDTOs){
                if(PCGDTO.getTitle().equals(PCDTO.getCourse().getTitle())
                        && PCGDTO.getCode().equals(PCDTO.getCourse().getCode())){
                    PCGDTO.setPeriodAndCredits(major,PCDTO);
                    existsCourse = true;
                    break;
                }
            }
            if(!existsCourse) {
                PlanCourseGridDTO PCGDTO = new PlanCourseGridDTO(PCDTO);
                PCGDTO.setPeriodAndCredits(major, PCDTO);
                PCGDTOs.add(PCGDTO);
            }
            existsCourse = false;
        }
        return PCGDTOs;
    }
    public static final List<PlanCourseGridDTO> mergePlanSpecsAndCreateGridDTO(List<PlanSpecDTO> PCDTOs,Major major) {
        List<PlanCourseGridDTO> PCGDTOs = new ArrayList();
        boolean existsCourse = false;
        for (PlanSpecDTO PCDTO : PCDTOs) {
            for(PlanCourseGridDTO PCGDTO :PCGDTOs){
                if((PCGDTO.getTitle() == null && PCDTO.getCourseClass()!=null && PCGDTO.getCourseClass().equals(PCDTO.getCourseClass().getTitle())) ||
                        PCGDTO.getTitle() !=null && PCDTO.getCourseClass()!=null && PCGDTO.getTitle().equals(PCDTO.getCourseClass().getTitle()) ||
                        PCGDTO.getTitle() !=null && PCDTO.getCourseType() !=null && PCGDTO.getTitle().equals(PCDTO.getCourseType().getTitle())){
                    PCGDTO.setPeriodAndCredits(major,PCDTO);
                    existsCourse = true;
                    break;
                }
            }
            if(!existsCourse) {
                PlanCourseGridDTO PCGDTO = new PlanCourseGridDTO(PCDTO);
                PCGDTO.setPeriodAndCredits(major, PCDTO);
                PCGDTOs.add(PCGDTO);
            }
            existsCourse = false;
        }
        return PCGDTOs;
    }
}
