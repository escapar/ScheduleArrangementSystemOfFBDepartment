package cn.edu.shnu.fb.interfaces.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.interfaces.assembler.ImpAssembler;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/8.
 */
@RequestMapping("/api")
@RestController
public class LocatorFacade {
    @Autowired
    LocatorRepository locatorRepository;
    @ResponseBody
    @RequestMapping(value="/l/m/{majorId}/t/{termCount}/cc/{courseClassId}/ct/{courseTypeId}",method= RequestMethod.GET)
    public Locator getLocators(@PathVariable Integer majorId,@PathVariable Integer termCount,@PathVariable Integer courseClassId , @PathVariable Integer courseTypeId){
        return locatorRepository.getLocatorByMajorIdAndTermCountAndCourseClassIdAndCourseTypeId(majorId, termCount, courseClassId , courseTypeId);
    }

    @ResponseBody
    @RequestMapping(value="/l/m/{majorId}/t/{termCount}",method= RequestMethod.GET)
    public List<Locator> getLocatorsByMajorAndTerm(@PathVariable Integer majorId,@PathVariable Integer termCount){
        return locatorRepository.getLocatorByMajorIdAndTermCount(majorId, termCount);
    }

    @ResponseBody
    @RequestMapping(value="/l/e/m/{majorId}/t/{termCount}",method= RequestMethod.GET) // e for electable
    public List<Locator> getLocatorsElectableByMajorAndTerm(@PathVariable Integer majorId,@PathVariable Integer termCount){
        return locatorRepository.getLocatorElectableByMajorIdAndTermCount(majorId, termCount);
    }
}
