package cn.edu.shnu.fb.interfaces.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;

/**
 * Created by bytenoob on 15/11/10.
 */
@RestController
@RequestMapping("/api")
public class IOFacade {
    @RequestMapping(value = "/o", method = RequestMethod.GET)
    public ModelAndView downloadExcel() {

       // List<ImpExcelHeaderDTO> impExcelDTOs = new ArrayList<ImpExcelHeaderDTO>();
        // excelView dengan data list impExcelDTOs akan diterima oleh resolver exvelView
        List<ImpExcelGridDTO> POLists = new ArrayList<>();
        POLists.add(new ImpExcelGridDTO("a","b","c",1,2,3,"d","e","f","g","h"));
        POLists.add(new ImpExcelGridDTO("d","eF","f",6,6,6,"gg","wer","d","qwer","ttt"));
        POLists.add(new ImpExcelGridDTO("a","b","c",1,2,3,"d","e","f","g","h"));
        POLists.add(new ImpExcelGridDTO("d","eSDF","f",8,6,6,"gg","wer","d","qwer","ttt"));
        POLists.add(new ImpExcelGridDTO("a","b","c",9,2,3,"d","e","f","g","h"));
        POLists.add(new ImpExcelGridDTO("d","e","f",5,6,6,"gg","wer","d","qwer","ttt"));
        POLists.add(new ImpExcelGridDTO("a","bWEF","c",13,2,3,"d","e","fWEF","g","h"));
        POLists.add(new ImpExcelGridDTO("dEFW","eW","f",62,6,6,"gg","wer","d","qwer","ttt"));
        POLists.add(new ImpExcelGridDTO("a","b","c",135,2,3,"d","e","f","g","h"));
        POLists.add(new ImpExcelGridDTO("d","e","fDGF",65,6,6,"gg","wer","d","qwer","ttt"));

        ImpExcelDTO ieDTO = new ImpExcelDTO(new ImpExcelHeaderDTO(1,2,3,4,5,6,7,8,9,10,11),POLists);
        return new ModelAndView("excelView", "impExcelDTOs", ieDTO);
    }

}

