package cn.edu.shnu.fb.infrastructure.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.common.CourseType;

/**
 * Created by bytenoob on 15/11/4.
 */
public interface CourseTypeDao extends PagingAndSortingRepository<CourseType,Integer> {
    CourseType findByTitleLike(String title);
}
