package cn.edu.shnu.fb.domain.course;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bytenoob on 15/11/4.
 */
public interface CourseRepository extends PagingAndSortingRepository<Course,Integer> {
    Course findCourseByTitle(String title);
    Course findCourseById(Integer Id);

}
