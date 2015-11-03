package cn.edu.shnu.fb.domain.imp;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface ImpCourseRepository extends PagingAndSortingRepository<ImpCourse,Integer> {
    List<ImpCourse> findImpCoursesByImpTermAndImpMajor(Term term,Major major);
}
