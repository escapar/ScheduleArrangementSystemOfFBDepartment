package cn.edu.shnu.fb.infrastructure.poi;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;
import cn.edu.shnu.fb.interfaces.assembler.IOAssembler;

/**
 * Created by bytenoob on 15/11/10.
 */
public class ExcelView extends AbstractXlsView {

    @Autowired
    ImpDao impDao;
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            Workbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"result.xls\"");
           // InputStream is= this.getClass().getResourceAsStream("impTemplate.xls");
        InputStream is = this.getClass().getResourceAsStream(request.getContextPath() + "/impTemplate.xls");
        ExcelTemplate template = ExcelTemplate.newInstance(is);
        ImpExcelDTO imp = (ImpExcelDTO)model.get("impExcelDTOs");
        ImpExcelHeaderDTO headerDTO = imp.getHeader();
        Map<String,Object> map = new HashMap<>();
        IOAssembler.flushParams(map, headerDTO);
        template.replaceParameters(map);

        List<ImpExcelGridDTO> iePodtos = imp.getPodto();
        ArrayList<Map> mapList = new ArrayList<>();
        for(ImpExcelGridDTO dto : iePodtos) {
            map = new HashMap<>();
            IOAssembler.flushParams(map, dto);
            mapList.add(map);
        }
        template.createRowByHashMap(mapList, "#PO");

        template.createRowByHashMap(mapList,"#MO");

        template.createRowByHashMap(mapList,"#RE");

        workbook = template.getWorkbook();
        /*
        List<ImpExcelHeaderDTO> impExcelDTOs = (List<ImpExcelHeaderDTO>) model.get("impExcelDTOs");

        Sheet sheet = workbook.createSheet("ImpExcelHeaderDTO List");
        sheet.setDefaultColumnWidth(30);

        CellStyle style = workbook.createCellStyle();

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Id");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Nama");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("Alamat");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Bagian");
        header.getCell(3).setCellStyle(style);

        int baris = 1;

        for (ImpExcelHeaderDTO impExcelDTO : impExcelDTOs) {
            Row dataBaris = sheet.createRow(baris++);
            dataBaris.createCell(0).setCellValue(impExcelDTO.getId());
            dataBaris.createCell(1).setCellValue(impExcelDTO.getNama());
            dataBaris.createCell(2).setCellValue(impExcelDTO.getAlamat());
            dataBaris.createCell(3).setCellValue(impExcelDTO.getBagian());
        }*/
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

}