package cn.edu.shnu.fb.interfaces.facade;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.shnu.fb.application.LogService;
import cn.edu.shnu.fb.domain.major.MajorRepository;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.interfaces.dto.*;

import org.apache.poi.ss.usermodel.Workbook;
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
import cn.edu.shnu.fb.application.SalaryService;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.term.TermRepository;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.plan.PlanRepository;

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
    SalaryService salaryService;

    @Autowired
    LogService logService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    MajorRepository majorRepository;

    @Autowired
    ImpRepository impRepository;

    @Autowired
    TermRepository termRepository;

    @RequestMapping(value = "/o/i/m/{majorId}/t/{termCount}", method = RequestMethod.GET)
    public ModelAndView downloadImpExcel(@PathVariable Integer majorId, @PathVariable Integer termCount) {
        ImpExcelDTO res = excelService.generateImpExcelDTO(majorId, termCount);
        return new ModelAndView("excelView", "impExcelDTOs", res);
    }

    public class CopyStream extends ByteArrayOutputStream {
        public CopyStream(int size) { super(size); }

        /**
         * Get an input stream based on the contents of this output stream.
         * Do not use the output stream after calling this method.
         * @return an {@link InputStream}
         */
        public InputStream toInputStream() {
            return new ByteArrayInputStream(this.buf, 0, this.count);
        }
    }

    @RequestMapping(value = "/o/i/t/{termYear}/{termPart}", method = RequestMethod.GET)
    public void downloadAllImpExcel(@PathVariable Integer termYear, @PathVariable Integer termPart,OutputStream os, HttpServletRequest r) throws IOException{
        Iterable<Major> majors = majorRepository.findAll();
        ZipOutputStream zip = new ZipOutputStream(os);
        Term term = termRepository.findTermByYearAndPart(termYear, termPart);
        for(Major m : majors){
            int termCount = (term.getYear() - m.getGrade())*2 + term.getPart();
            if(termCount<=0) continue;
            File file = new File(m.getGrade()+"级"+m.getMajorType().getTitle()+".xls");
            CopyStream osf = new CopyStream(20480);
            ImpExcelDTO ieDTO = excelService.generateImpExcelDTO(m.getId(), termCount);
            excelService.generateImpWorkbook(ieDTO,r.getContextPath()+ "/impTemplate.xls").write(osf);
            zip.putNextEntry(new ZipEntry(file.getName()));
            InputStream is = osf.toInputStream();
            int length;

            byte[] b = new byte[2048];

            while((length = is.read(b)) > 0) {
                zip.write(b, 0, length);
            }
            zip.closeEntry();
            is.close();
            osf.close();
        }
        zip.close();
        try {
            os.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        finally{
            os.close();
        }
    }

    @RequestMapping(value = "/i/p/m/{majorId}", method = RequestMethod.POST)
    public void importPlanExcel(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer majorId) throws Exception {
        List<GridEntityDTO> geDTOs = null;
        geDTOs = excelService.generatePlan(file.getInputStream());
        planRepository.updatePlansByExcelAndMajorId(majorId, geDTOs);
        logService.action("培养方案", "导入");
    }
    
    @RequestMapping(value = "/i/s/{type}", method = RequestMethod.POST)
    public void importSalaryExcel(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer type) throws Exception{
        // type : 0 文修 1 副修 2 研究生
        List<SalaryDTO> salaryDTOs = excelService.generateSalaryDTOs(file.getInputStream(),type);
        salaryService.persistSalaryDTOs(salaryDTOs);
        logService.action("系统外课程数据","导入");
    }

    @RequestMapping(value = "/i/p/e/m/{majorId}", method = RequestMethod.POST)
    public void importElectableCourse(@RequestParam(value = "file[0]", required = false) MultipartFile file, @PathVariable Integer majorId) throws Exception {
        List<GridEntityDTO> geDTOs = null;
        geDTOs = wordService.generatePlan(file.getInputStream());
        planRepository.updatePlanCoursesByWordAndMajorId(majorId, geDTOs);
        logService.action("选修课", "导入");
    }
    @RequestMapping(value = "/o/i/m/{majorId}", method = RequestMethod.GET)
    public ModelAndView downloadImpCreditsExcel(@PathVariable Integer majorId) {
        ImpCreditsDTO res = excelService.generateImpCreditsDTO(majorId);
        return new ModelAndView("impCreditsExcelView", "impCreditsDTOS", res);
    }
    @RequestMapping(value = "/o/s/term/{termYear}/{termPart}", method = RequestMethod.GET)
    public ModelAndView downloadSalaryExcel(@PathVariable Integer termYear , @PathVariable Integer termPart) {
        Term term = termRepository.findTermByYearAndPart(termYear , termPart);
        SalaryExcelDTO res=salaryService.getSalaryExcelDTO(term.getId());
        return new ModelAndView("salaryExcelView", "SalaryExcelDTOS", res);
    }
/*
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public LogDTO getLog(HttpServletRequest request) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(request.getContextPath() + "/log.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line+'\n');
        }
        LogDTO log = new LogDTO();
        log.setLog(buffer.toString());
        return log;
    }*/

}

