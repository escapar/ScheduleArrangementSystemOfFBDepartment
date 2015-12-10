package cn.edu.shnu.fb.interfaces.dto;

import java.util.ArrayList;
import java.util.List;

import cn.edu.shnu.fb.domain.Imp.Imp;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/12/5.
 */
public class MergePageEntityDTO {
    Integer teacherId;
    String teacherName;
    List<List<Imp>> imps = new ArrayList<>(); //Only for imps in same course

    public MergePageEntityDTO(Teacher teacher) {
        teacherId = teacher.getId();
        teacherName = teacher.getName();
    }

    public MergePageEntityDTO(List<Teacher> teachers) {
        for(Teacher teacher : teachers) {
            teacherId = teacher.getId();
            if(teacherName == null){
                teacherName = teacher.getName();
            }else {
                teacherName += ','+teacher.getName();
            }
        }
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(final Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(final String teacherName) {
        this.teacherName = teacherName;
    }

    public List<List<Imp>> getImps() {
        return imps;
    }

    public void setImps(final List<List<Imp>> imps) {
        this.imps = imps;
    }

    public void addImpList(final List<Imp> iList){
        imps.add(iList);
    }

    public void removeImpList(final List<Imp> iList){
        imps.remove(iList);
    }
}
