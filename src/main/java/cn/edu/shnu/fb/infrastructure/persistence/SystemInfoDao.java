package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.common.Locator;
import cn.edu.shnu.fb.domain.common.SystemInfo;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/7.
 */
public interface SystemInfoDao extends PagingAndSortingRepository<SystemInfo,Integer> {
    SystemInfo findByYear(Integer year);

}
