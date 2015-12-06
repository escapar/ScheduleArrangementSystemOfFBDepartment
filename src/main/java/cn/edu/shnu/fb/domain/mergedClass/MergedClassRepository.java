package cn.edu.shnu.fb.domain.mergedClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.infrastructure.persistence.MergedClassDao;

/**
 * Created by bytenoob on 15/12/4.
 */
@Repository
public class MergedClassRepository {
    @Autowired
    MergedClassDao mergedClassDao;
}
