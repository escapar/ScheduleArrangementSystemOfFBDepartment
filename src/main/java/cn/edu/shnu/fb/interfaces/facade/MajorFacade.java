package cn.edu.shnu.fb.interfaces.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.major.MajorRepository;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/21.
 */

@RequestMapping("/api")
@RestController
public class MajorFacade {
    @Autowired
    MajorRepository majorRepository;
    @ResponseBody
    @RequestMapping(value="/m/init",method= RequestMethod.POST)
    public void initMajor (@RequestBody Major major){
        majorRepository.initMajor(major);
    }

    @ResponseBody
    @RequestMapping(value="/m",method= RequestMethod.GET)
    public Iterable<Major> initMajor (){
        return majorRepository.findAll();
    }
}
