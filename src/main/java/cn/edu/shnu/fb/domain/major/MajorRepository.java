package cn.edu.shnu.fb.domain.major;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bytenoob on 15/11/1.
 */
public interface MajorRepository extends PagingAndSortingRepository<Major,Integer> {
    Major findMajorByGradeAndTitleLike(Integer grade,String title);
    List<Major> findMajorsByGrade(Integer grade);
}
