package cn.edu.shnu.fb.domain.major;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.common.LocatorRepository;
import cn.edu.shnu.fb.infrastructure.persistence.MajorDao;

/**
 * Created by bytenoob on 15/11/21.
 */
@Repository
public class MajorRepository {
    @Autowired
    MajorDao majorDao;
    @Autowired
    LocatorRepository locatorRepository;
    public void initMajor(Major major){
        try{
        majorDao.save(major);
        }catch (Exception e){

        }finally {
            Major majorP = majorDao.findMajorByGradeAndTitle(major.getGrade(),major.getTitle());
            locatorRepository.initLocators(majorP.getId());
        }

    }

    public Iterable<Major> findAll(){
        return majorDao.findAll();
    }
}
