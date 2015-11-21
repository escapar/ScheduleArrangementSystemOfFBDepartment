package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.major.Major;

/**
 * Created by bytenoob on 15/11/1.
 */
public interface MajorDao extends PagingAndSortingRepository<Major,Integer> {
    Major findMajorByGradeAndTitleLike(Integer grade,String title); // 注意不要在浏览器链接里出现中文
    Major findMajorByGradeAndTitle(Integer grade,String title); // 注意不要在浏览器链接里出现中文

    List<Major> findByGrade(Integer grade);
}
