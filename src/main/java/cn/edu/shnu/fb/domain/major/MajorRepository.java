package cn.edu.shnu.fb.domain.major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.Imp.ImpComment;
import cn.edu.shnu.fb.domain.Imp.ImpRepository;
import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.infrastructure.persistence.ImpCommentDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;
import cn.edu.shnu.fb.infrastructure.persistence.MajorTypeDao;

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
    MajorTypeDao majorTypeDao;
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
