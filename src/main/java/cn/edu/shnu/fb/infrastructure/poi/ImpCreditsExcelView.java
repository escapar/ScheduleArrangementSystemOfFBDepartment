package cn.edu.shnu.fb.infrastructure.poi;

import cn.edu.shnu.fb.interfaces.assembler.IOAssembler;
import cn.edu.shnu.fb.interfaces.dto.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouziyi on 15/11/21.
 */
public class ImpCreditsExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"creditsresult.xls\"");
        // InputStream is= this.getClass().getResourceAsStream("impTemplate.xls");
        InputStream is = this.getClass().getResourceAsStream(request.getContextPath() + "/impCreditsTemplate.xls");
        ExcelTemplate template = ExcelTemplate.newInstance(is);
        ImpCreditsDTO impcredits =(ImpCreditsDTO)model.get("impCreditsDTOS");


        CreditsHeaderDTO headerdto = impcredits.getHeaderdto();
        Map<String,Object> map = new HashMap<>();
        IOAssembler.flushParams(map, headerdto);
        template.replaceParametersBykeyword(map,"#");

        CreditsDTO fircredto=impcredits.getFircredto();
        Map<String,Object> fircremap = new HashMap<>();
        IOAssembler.flushParams(fircremap, fircredto);
        template.replaceParametersBykeyword(fircremap,"#1");

        CreditsDTO seccredto=impcredits.getSeccredto();
        Map<String,Object> seccremap = new HashMap<>();
        IOAssembler.flushParams(seccremap, seccredto);
        template.replaceParametersBykeyword(seccremap,"#2");

        CreditsDTO thicredto=impcredits.getThicredto();
        Map<String,Object> thicremap = new HashMap<>();
        IOAssembler.flushParams(thicremap, thicredto);
        template.replaceParametersBykeyword(thicremap,"#3");

        CreditsDTO foucredto=impcredits.getFoucredto();
        Map<String,Object> foucremap = new HashMap<>();
        IOAssembler.flushParams(foucremap, foucredto);
        template.replaceParametersBykeyword(foucremap,"#4");

        CreditsDTO fivcredto=impcredits.getFivcredto();
        Map<String,Object> fivcremap = new HashMap<>();
        IOAssembler.flushParams(fivcremap, fivcredto);
        template.replaceParametersBykeyword(fivcremap,"#5");

        CreditsDTO sixcredto=impcredits.getSixcredto();
        Map<String,Object> sixcremap = new HashMap<>();
        IOAssembler.flushParams(sixcremap, sixcredto);
        template.replaceParametersBykeyword(sixcremap,"#6");

        CreditsDTO sevcredto=impcredits.getSevcredto();
        Map<String,Object> sevcremap = new HashMap<>();
        IOAssembler.flushParams(sevcremap, sevcredto);
        template.replaceParametersBykeyword(sevcremap,"#7");

        CreditsDTO eigcredto=impcredits.getEigcredto();
        Map<String,Object> eigcremap = new HashMap<>();
        IOAssembler.flushParams(eigcremap, eigcredto);
        template.replaceParametersBykeyword(eigcremap,"#8");

        SumCreditsDTO sumcreditsdto=impcredits.getSumcreditsdto();
        Map<String,Object> sumcremap = new HashMap<>();
        IOAssembler.flushParams(sumcremap, sumcreditsdto);
        template.replaceParametersBykeyword(sumcremap,"#");

        workbook = template.getWorkbook();
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }



}
