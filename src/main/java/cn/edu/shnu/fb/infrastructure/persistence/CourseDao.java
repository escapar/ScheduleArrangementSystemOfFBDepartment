package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.course.Course;
import cn.edu.shnu.fb.domain.user.Teacher;

/**
 * Created by bytenoob on 15/11/4.
 */
public interface CourseDao extends PagingAndSortingRepository<Course,Integer> {
    Course findByTitle(String title); //不推荐
    Course findByCode(String code); //不推荐
    List<Course> findByCodeAndTitle(String code , String title);
    List<Course> findByTitleLike(String name);

}
