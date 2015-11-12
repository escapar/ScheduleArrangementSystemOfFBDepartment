package cn.edu.shnu.fb.interfaces.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.shnu.fb.application.ExcelService;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;

/**
 * Created by bytenoob on 15/11/10.
 */
@RestController
@RequestMapping("/api")
public class IOFacade {
    @Autowired
    ExcelService excelService;
    @RequestMapping(value = "/o/i/m/{majorId}/t/{termCount}", method = RequestMethod.GET)
    public ModelAndView downloadImpExcel(@PathVariable Integer majorId,@PathVariable Integer termCount) {
        ImpExcelDTO res = excelService.generateImpExcelDTO(majorId, termCount);
        return new ModelAndView("excelView", "impExcelDTOs", res);
    }

}

