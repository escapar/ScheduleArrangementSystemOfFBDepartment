package cn.edu.shnu.fb.interfaces.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanCourse;
import cn.edu.shnu.fb.domain.plan.PlanSpec;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/10/31.
 */
public class PlanAssembler {

    public final static List<GridEntityDTO> PlanCourseToGridEntityDTO(Iterable<PlanCourse> planCourses){
        if(planCourses!=null) {
            List<GridEntityDTO> resList = new ArrayList<>();
            for (PlanCourse pc : planCourses) {
                if(pc!=null) {
                    resList.add(new GridEntityDTO(pc));
                }
            }
            return resList;
        }else{
            return null;
        }
    }

    public final static List<GridEntityDTO> PlanSpecToGridEntityDTO(Iterable<PlanSpec> planSpecs){
        if(planSpecs!=null) {
            List<GridEntityDTO> resList = new ArrayList<>();
            for (PlanSpec ps : planSpecs) {
                resList.add(new GridEntityDTO(ps));
            }
            return resList;
        }else{
            return null;
        }
    }

    public static final List<GridEntityDTO> mergePlanCoursesAndCreateGridEntityDTO(List<PlanCourse> PCs) {
        List<GridEntityDTO> resList = new ArrayList();
        if(PCs.size()>0) {
            Major major = PCs.get(0).getLocator().getMajor();
            boolean existsCourse = false;
            for (PlanCourse pc : PCs) {
                if (pc.getLocator().getTerm() != null) { // to ensure it's not a
                    for (GridEntityDTO entity : resList) {
                        if (entity.getCourseId() == pc.getCourse().getId()) {
                            entity.setPeriodAndCredits(major, pc);
                            existsCourse = true;
                            break;
                        }
                    }
                    if (!existsCourse) {
                        GridEntityDTO tmpEntity = new GridEntityDTO(pc);
                        tmpEntity.setPeriodAndCredits(major, pc);
                        resList.add(tmpEntity);
                    }
                    existsCourse = false;
                }
            }
        }
        return resList;
    }

    public static final List<GridEntityDTO> mergePlanSpecsAndCreateGridEntityDTO(List<PlanSpec> PSs) {
        List<GridEntityDTO> resList = new ArrayList();
        if(PSs.size()>0) {
            Major major = PSs.get(0).getLocator().getMajor();
            boolean existsCourse = false;
            for (PlanSpec ps : PSs) {
                for (GridEntityDTO entity : resList) {
                    if (hasSameReference(ps, entity)) {
                        entity.setPeriodAndCredits(major, ps);
                        existsCourse = true;
                        break;
                    }
                }
                if (!existsCourse) {
                    GridEntityDTO tmpEntity = new GridEntityDTO(ps);
                    tmpEntity.setPeriodAndCredits(major, ps);
                    resList.add(tmpEntity);
                }
                existsCourse = false;
            }
        }
        return resList;
    }

    public static final List<GridEntityDTO> mergePlanCoursesAndPlanSpecs(List<PlanCourse> PCs, List<PlanSpec> PSs){
        if(PCs != null && PSs!=null) {
            List<GridEntityDTO> entities = PlanAssembler.mergePlanCoursesAndCreateGridEntityDTO(PCs);
            entities.addAll(PlanAssembler.mergePlanSpecsAndCreateGridEntityDTO(PSs));
            return entities;
        }else{
            return null;
        }
    }

    private static boolean hasSameReference(PlanSpec PS , GridEntityDTO GE){
        return (GE.getTitle() == null && PS.getLocator().getCourseClass()!=null && GE.getCourseClass().equals(PS.getLocator().getCourseClass().getTitle())) ||
                GE.getTitle() !=null && PS.getLocator().getCourseClass()!=null && GE.getTitle().equals(PS.getLocator().getCourseClass().getTitle()) ||
                GE.getTitle() !=null && PS.getLocator().getCourseType() !=null && GE.getTitle().equals(PS.getLocator().getCourseType().getTitle());
    }
}
