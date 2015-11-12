package cn.edu.shnu.fb.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.CourseExam;
import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.ImpDao;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelGridDTO;
import cn.edu.shnu.fb.interfaces.dto.ImpExcelHeaderDTO;

/**
 * Created by bytenoob on 15/11/10.
 */
@Service


public class ExcelService {
    @Autowired
    ImpRepository impRepository;
    public ImpExcelDTO generateImpExcelDTO(int majorId, int termCount){
        List<ImpExcelGridDTO> POLists = new ArrayList<>();
        List<Imp> imps = impRepository.getAllImpByMajorIdAndTermCount(majorId,termCount);
        for(Imp imp : imps){
            POLists.add(toExcelDTO(imp));
        }
        ImpExcelDTO res = new ImpExcelDTO(new ImpExcelHeaderDTO(1,2,3,4,5,6,7,8,9,10,11),POLists);
        return res;
    }

    public ImpExcelGridDTO toExcelDTO(Imp imp){
        Course course = imp.getCourse();
        CourseExam courseExam = imp.getCourseExam();
        Teacher teacher = imp.getTeacher();

        String courseCode = "";
        String courseTitle = "";
        String courseExamTitle = "";
        String isDegCouseTitle = "";
        String teacherCode = "";
        String teacherName = "";
        String teacherTitle = "";
        String comment = "";

        if(course != null){
            courseCode = imp.getCourse().getCode();
            courseTitle = imp.getCourse().getTitle();
        }

        if(courseExam != null){
            courseExamTitle = imp.getCourseExam().getTitle();
        }

        if(imp.getIsDegCourse() == 1){
            isDegCouseTitle = "是";
        } else {
            isDegCouseTitle = "否";
        }
        return new ImpExcelGridDTO(courseCode,courseTitle,courseExamTitle,imp.getCredits(),imp.getPeriodWeeks(),
                imp.getPeriodHours(),isDegCouseTitle,teacherCode,teacherName,teacherTitle,comment);
    }
}
