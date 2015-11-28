package cn.edu.shnu.fb.interfaces.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.term.TermRepository;

/**
 * Created by bytenoob on 15/11/26.
 */
@RequestMapping("/api")
@RestController
public class TermFacade {
    @Autowired
    TermRepository termRepository;
    @ResponseBody
    @RequestMapping(value="/t/{grade}",method= RequestMethod.GET)
    public Term initMajor (@PathVariable Integer grade){
        return termRepository.findTermByGradeAndTermCount(grade,1);
    }
}
