package cn.edu.shnu.fb.domain.major;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cn.edu.shnu.fb.domain.Imp.ImpComment;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.infrastructure.persistence.ImpCommentDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorTypeDao;
import cn.edu.shnu.fb.infrastructure.persistence.TeacherDao;

/**
 * Created by bytenoob on 15/11/21.
 */
@Repository
public class MajorRepository {
    @Autowired
    MajorDao majorDao;
    @Autowired
    LocatorRepository locatorRepository;
    @Autowired
    ImpCommentDao impCommentDao;
    @Autowired
    TeacherDao teacherDao;
    @Autowired
    MajorTypeDao majorTypeDao;

    public void changeRespRight(Integer respId , List<MajorType> majorTypes){
        Teacher teacher = teacherDao.findOne(respId);
        if(teacher!=null){
            teacher.setMajorTypes(majorTypes);
            teacherDao.save(teacher);
        }
    }
    public void initMajor(Major major){
        try{
            Major majorP = majorDao.save(major);
            locatorRepository.initLocators(majorP.getId());
        }catch (Exception e){
            Major majorP = majorDao.findMajorByGradeAndMajorTypeTitle(major.getGrade(), major.getMajorType().getTitle());
            locatorRepository.initLocators(majorP.getId());
        }
    }

    public void modifyMajor(Major major){
        majorDao.save(major);
    }

    public List<Major> findByResponsableTeacherId(Integer id){
        Teacher teacher = teacherDao.findOne(id);
        List<Major> res = new ArrayList<>();
        if(teacher!=null){
            List<MajorType> mts = teacher.getMajorTypes();
            for(MajorType mt : mts){
                res.addAll(majorDao.findMajorByMajorType(mt));
            }
        }
        return res;
    }

    public List<MajorType> findMajorTypeByResponsableTeacherId(Integer id){
        Teacher teacher = teacherDao.findOne(id);
        if(teacher!=null){
            return teacher.getMajorTypes();
        }else{
            return null;
        }
    }
    public Iterable<Major> findAll(){
        return majorDao.findAll();
    }

    public Iterable<MajorType> findAllMajorType(){
        return majorTypeDao.findAll();
    }

    public void updateMajorType(MajorType majorType){
        if(majorTypeDao.findByMajorCodeAndTitle(majorType.getMajorCode(),majorType.getTitle()) == null) {
            majorTypeDao.save(majorType);
        }
    }

    public void deleteByMajorId(int majorId){
        Major major = majorDao.findOne(majorId);
        locatorRepository.deleteLocatorsByMajorId(majorId);
        if(major != null) {
            Iterable<ImpComment> impComments = impCommentDao.findByMajor(major);
            for (ImpComment ic : impComments) {
                impCommentDao.delete(ic);
            }
            majorDao.delete(major);
        }
    }
}
