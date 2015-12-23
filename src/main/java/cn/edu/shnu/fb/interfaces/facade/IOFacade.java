package cn.edu.shnu.fb.interfaces.facade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.Imp.Salary;
import cn.edu.shnu.fb.domain.Imp.SalaryRepository;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.interfaces.dto.ImpCreditsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.shnu.fb.application.ExcelService;
import cn.edu.shnu.fb.application.WordService;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanRepository;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/11/10.
 */
@RestController
@RequestMapping("/api")
public class IOFacade {
    @Autowired
    ExcelService excelService;

    @Autowired
    WordService wordService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ImpRepository impRepository;

    @Autowired
    TermRepository termRepository;

    @Autowired
    SalaryRepository salaryRepository;
    @RequestMapping(value = "/o/i/m/{majorId}/t/{termCount}", method = RequestMethod.GET)
    public ModelAndView downloadImpExcel(@PathVariable Integer majorId,@PathVariable Integer termCount) {
        //impRepository.persistImpComment(majorId,termCount,comment);
        ImpExcelDTO res = excelService.generateImpExcelDTO(majorId, termCount);
        return new ModelAndView("excelView", "impExcelDTOs", res);
    }

    @RequestMapping(value = "/i/p/m/{majorId}", method = RequestMethod.POST)
    public void importPlanExcel(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer majorId) {
        List<GridEntityDTO> geDTOs = null ;
        try {
            geDTOs = excelService.generatePlan(file.getInputStream());
            planRepository.updatePlansByExcelAndMajorId(majorId, geDTOs);
        }catch (Exception e){

        }
    }

    @RequestMapping(value = "/s/term/{termYear}/{termPart}", method = RequestMethod.POST)
    public void importAdditionalCourseExcel(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer termYear,@PathVariable Integer termPart) {
        Term term = termRepository.findTermByYearAndPart(termYear, termPart);
        try {

            List<SalaryDTO> salaryDTOs = excelService.generateSalaryDTOs(file.getInputStream());
            for(SalaryDTO s : salaryDTOs) {
                salaryRepository.persistSalary(s);
            }
        }catch (Exception e){

        }
    }

    @RequestMapping(value = "/i/p/e/m/{majorId}", method = RequestMethod.POST)
    public void importElectableCourse(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer majorId) {
        List<GridEntityDTO> geDTOs = null ;
        try {
            geDTOs = wordService.generatePlan(file.getInputStream());
            planRepository.updatePlanCoursesByWordAndMajorId(majorId, geDTOs);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
    @RequestMapping(value = "/o/i/m/{majorId}", method = RequestMethod.GET)
    public ModelAndView downloadImpCreditsExcel(@PathVariable Integer majorId) {
        ImpCreditsDTO res = excelService.generateImpCreditsDTO(majorId);
        return new ModelAndView("impCreditsExcelView", "impCreditsDTOS", res);
    }
}

