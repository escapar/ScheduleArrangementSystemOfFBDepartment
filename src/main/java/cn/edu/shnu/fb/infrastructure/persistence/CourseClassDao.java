package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseClass;
import cn.edu.shnu.fb.domain.common.CourseType;

/**
 * Created by bytenoob on 15/11/4.
 */
public interface CourseClassDao extends PagingAndSortingRepository<CourseClass,Integer>{
    List<CourseClass> findByTitleLike(String title);
    List<CourseClass> findByTitleNotLike(String title);

}
