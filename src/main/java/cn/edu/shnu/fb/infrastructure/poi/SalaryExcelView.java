package cn.edu.shnu.fb.infrastructure.poi;

import cn.edu.shnu.fb.application.SalaryService;
import cn.edu.shnu.fb.interfaces.assembler.IOAssembler;
import cn.edu.shnu.fb.interfaces.dto.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouziyi on 15/12/18.
 */
public class SalaryExcelView extends AbstractXlsView {
    @Autowired
    SalaryService salaryService;

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"salaryresult.xls\"");
        response.setHeader("Content-type", "application/vnd.ms-excel");
        response.setHeader("Set-Cookie","fileDownload=true; path=/");        // InputStream is= this.getClass().getResourceAsStream("impTemplate.xls");
        InputStream is = this.getClass().getResourceAsStream(request.getContextPath() + "/salaryTemplate.xls");
        ExcelTemplate template = ExcelTemplate.newInstance(is);
        SalaryExcelDTO salaryExcels =(SalaryExcelDTO)model.get("SalaryExcelDTOS");

        SalaryHeaderDTO header =salaryExcels.getSalaryHeaderDTO();
        Map<String,Object> headermap = new HashMap<>();
        IOAssembler.flushParams(headermap, header);
        template.replaceParametersBykeyword(headermap, "#");

        List<SalaryDTO> salaryDTOs=salaryExcels.getSalaryDTOs();
        Map<String,Object> map=new HashMap<>();
        ArrayList<Map> mapList = new ArrayList<>();
        for(SalaryDTO dto : salaryDTOs) {
            map = new HashMap<>();
            IOAssembler.flushParams(map, dto);
            mapList.add(map);
        }

        template.createRowByHashMap(mapList, "#A",0);



        workbook = template.getWorkbook();
        workbook.setSheetName(0,header.getFromyear() + "-" + header.getToyear() + "学年第"+header.getPart()+"学期商学院教师课时费明细(核对稿)");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }



}
