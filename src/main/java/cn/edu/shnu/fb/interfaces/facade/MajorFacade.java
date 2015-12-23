package cn.edu.shnu.fb.interfaces.facade;

import cn.edu.shnu.fb.application.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.major.MajorRepository;
import cn.edu.shnu.fb.domain.major.MajorType;
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
    @Autowired
    LogService logService;
    @ResponseBody
    @RequestMapping(value="/m/init",method= RequestMethod.POST)
    public void initMajor (@RequestBody Major major){
        majorRepository.initMajor(major);
        logService.action("专业","初始化");
    }

    @RequestMapping(value="/m/modify",method= RequestMethod.POST)
    public void modifyMajor (@RequestBody Major major) {
        majorRepository.modifyMajor(major);
        logService.action("专业", "修改");
    }
    @ResponseBody
    @RequestMapping(value="/mt/init",method= RequestMethod.POST)
    public void initMajorType (@RequestBody MajorType majorType){
        majorRepository.updateMajorType(majorType);
        logService.action("专业类型", "初始化");
    }

    @ResponseBody
    @RequestMapping(value="/m",method= RequestMethod.GET)
    public Iterable<Major> getAllMajors (){
        return majorRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(value="/mt",method= RequestMethod.GET)
    public Iterable<MajorType> getAllMajorTypes (){
        return majorRepository.findAllMajorType();
    }


    @ResponseBody
    @RequestMapping(value="/m/{majorId}/delete",method= RequestMethod.GET)
    public void deleteMajor (@PathVariable Integer majorId){
        majorRepository.deleteByMajorId(majorId);
        logService.action("专业","删除");
    }

    @ResponseBody
    @RequestMapping(value="/m/available",method= RequestMethod.GET)
    public Iterable<Major> findAvailable(){
        return majorRepository.findAll();
    }

}
