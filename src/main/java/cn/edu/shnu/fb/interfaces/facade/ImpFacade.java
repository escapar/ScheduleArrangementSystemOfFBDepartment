package cn.edu.shnu.fb.interfaces.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElementWrapper;

import cn.edu.shnu.fb.application.LogService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.application.CautionService;
import cn.edu.shnu.fb.application.ExcelService;
import cn.edu.shnu.fb.domain.Imp.ImpComment;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.infrastructure.persistence.LocatorDao;
import cn.edu.shnu.fb.interfaces.assembler.ImpAssembler;
import cn.edu.shnu.fb.interfaces.dto.CreditsDTO;
import cn.edu.shnu.fb.interfaces.dto.CreditsGridDTO;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;
import cn.edu.shnu.fb.interfaces.dto.MergeDTO;
import cn.edu.shnu.fb.interfaces.dto.MergePageEntityDTO;

/**
 * Created by bytenoob on 15/11/2.
 */


@RequestMapping("/api")
@RestController
public class ImpFacade {
    @Autowired
    ImpRepository impRepository;
    @Autowired
    CautionService cautionService;
    @Autowired
    ExcelService excelService;
    @Autowired
    LogService logService;
    @Autowired
    TermRepository termRepository;
    @ResponseBody
    @RequestMapping(value="/i/o/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // o for oblige
    public List<GridEntityDTO> getImpOblige(@PathVariable Integer majorId,@PathVariable Integer termCount){
        List<Imp> imps = impRepository.getObligeImpByMajorIdAndTermCount(majorId, termCount);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/e/m/{majorId}/t/{termCount}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getImpElectable(@PathVariable Integer majorId,@PathVariable Integer termCount){
        List<Imp> imps = impRepository.getElectableImpByMajorIdAndTermCount(majorId, termCount);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @Bean
    public MappingJackson2HttpMessageConverter JsonMapping(){
        MappingJackson2HttpMessageConverter jsonConverter=new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @ResponseBody
    @RequestMapping(value="/i/l/{locatorId}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getImpElectableByLocator(@PathVariable Integer locatorId){
        List<Imp> imps = impRepository.getImpByLocatorId(locatorId);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/m/{majorId}/cc/{courseClassId}/ct/{courseTypeId}/grid",method=RequestMethod.GET) // e for electable
    public List<GridEntityDTO> getElectedImp(@PathVariable Integer majorId , @PathVariable Integer courseClassId , @PathVariable Integer courseTypeId){
        List<Imp> imps = impRepository.getElectedImp(majorId, courseClassId, courseTypeId);
        return ImpAssembler.toGridEntityDTO(imps);
    }

    @ResponseBody
    @RequestMapping(value="/i/l/{locatorId}/update",method=RequestMethod.POST , consumes = "application/json")  //  l for location
    public void updateImpByLocator(@RequestBody List<GridEntityDTO> gridEntityDTOs,@PathVariable Integer locatorId){

        //add
        for(GridEntityDTO entity : gridEntityDTOs) {
            impRepository.updateImpByGridEntityAndLocatorId(entity, locatorId);
        }

        //delete
        List<Imp> currentImps = impRepository.getImpByLocatorId(locatorId);
        Iterable<Imp> differedImps ;
        if(currentImps.size() != gridEntityDTOs.size()) {
            if(gridEntityDTOs.size() != 0) {
                differedImps = ImpAssembler.differImpListWithGridEntityListByLocator(currentImps, gridEntityDTOs);
            }else{
                differedImps = currentImps;
            }
            for(Imp dImp :differedImps){
                impRepository.deleteImp(dImp);
            }
        }
        logService.action("执行计划","调整");
    }

    @ResponseBody
    @RequestMapping(value="/i/update/t/{termCount}",method=RequestMethod.POST , consumes = "application/json")  //  l for location
    public void updateImp(@RequestBody GridEntityDTO grid , @PathVariable Integer termCount){
         impRepository.updateImpByGridEntity(grid,termCount);
         logService.action("执行计划", "调整");
    }

    @ResponseBody
    @RequestMapping(value="/i/{impId}",method=RequestMethod.GET)
    public Imp findById(@PathVariable Integer impId){
        return impRepository.getImpById(impId);
    }

    @ResponseBody
    @RequestMapping(value="/i/comment/m/{majorId}/t/{termCount}/update",method=RequestMethod.POST , consumes = "application/json")
    public void updateImpComment(@PathVariable Integer majorId , @PathVariable Integer termCount,@RequestBody String comment){
        impRepository.persistImpComment(majorId, termCount, comment);
        logService.action("执行计划调整说明","更新");
    }

    @ResponseBody
    @RequestMapping(value="/i/comment/m/{majorId}/t/{termCount}",method=RequestMethod.GET)
    public String getImpComment(@PathVariable Integer majorId , @PathVariable Integer termCount){
        ImpComment res = impRepository.getImpCommentByMajorIdAndTermCount(majorId, termCount);
        if(res!=null){
            return res.getComment();
        }else{
            return null;
        }
    }


    @ResponseBody
    @RequestMapping(value="/i/e/caution/m/{majorId}/t/{termCount}",method=RequestMethod.GET)
    public ArrayList<String> getElectableCaution(@PathVariable Integer majorId,@PathVariable Integer termCount){
        ArrayList<String> electCaution=cautionService.getElectableCaution(majorId, termCount);
        return electCaution;
    }
    @ResponseBody
    @RequestMapping(value="/i/o/caution/m/{majorId}",method=RequestMethod.GET)
    public ArrayList<String> getOblidgeCaution(@PathVariable Integer majorId){
        ArrayList<String> obligeCaution=cautionService.getOblidgeCaution(majorId);
        return obligeCaution;
    }

    @ResponseBody
    @RequestMapping(value="/impdto/m/{majorId}/t/{termCount}",method=RequestMethod.GET)
    public ImpExcelDTO getExcelDtoImp(@PathVariable Integer majorId,@PathVariable Integer termCount){
        return excelService.generateImpExcelDTO(majorId,termCount);
    }

    @ResponseBody
    @RequestMapping(value="/creditsdto/m/{majorId}/t/{termCount}",method=RequestMethod.GET)
    public List<CreditsGridDTO> getExcelCreditsDtoImp(@PathVariable Integer majorId,@PathVariable Integer termCount){
        CreditsDTO dto = excelService.getCreditDTOByMajorAndTerm(majorId, termCount);
        List<CreditsGridDTO> CGDTOs = new ArrayList<>();
        if(dto!=null) {
            CGDTOs.add(new CreditsGridDTO(dto, 1));
            CGDTOs.add(new CreditsGridDTO(dto, 2));
        }
        return CGDTOs;
    }

    @ResponseBody
    @RequestMapping(value="/i/merge",method=RequestMethod.POST  , consumes = "application/json")
    public void mergeImps(@RequestBody List<MergeDTO> mergeDTO){
        impRepository.mergeImps(mergeDTO);
        logService.action("合并班级","请求");
    }

    @ResponseBody
    @RequestMapping(value="/i/merged",method=RequestMethod.GET)
    public List<MergePageEntityDTO> mergedImps(){
        return impRepository.getMergedImps(false);
    }

    @ResponseBody
    @RequestMapping(value="/i/merge/undo",method=RequestMethod.POST)
    public void undoMergeImps(@RequestBody List<MergeDTO> mergeDTO){
        impRepository.undoMergeImps(mergeDTO);
        logService.action("合并班级","撤销");
    }

    @ResponseBody
    @RequestMapping(value="/i/term/{termYear}/{termPart}/merge/dto",method=RequestMethod.GET)
    public List<MergePageEntityDTO> getImpsForMerge(@PathVariable Integer termYear,@PathVariable Integer termPart){
        Term term = termRepository.findTermByYearAndPart(termYear , termPart);
        return impRepository.getImpsForMerge(term);
    }

    @ResponseBody
    @RequestMapping(value="/i/merge/dto/verify",method=RequestMethod.GET)
    public List<MergePageEntityDTO> getImpsMergeForVerify(){
        return impRepository.getMergedImps(true);
    }

    @ResponseBody
    @RequestMapping(value="/i/merge/verify",method=RequestMethod.POST , consumes = "application/json")
    public void verifyMerge(@RequestBody List<MergeDTO> mergeDTO){
        impRepository.verifyMergeImps(mergeDTO);
        logService.action("合并班级","审核");
    }
}
