package cn.edu.shnu.fb.application;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.infrastructure.poi.ExcelTemplate;
import cn.edu.shnu.fb.infrastructure.poi.WordTemplate;
import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 * Created by bytenoob on 15/11/17.
 */
@Service

public class WordService {
    public List<GridEntityDTO> generatePlan(InputStream is){
        WordTemplate template = WordTemplate.newInstance(is);
        return template.getCourseGridEntity();
    }
}
