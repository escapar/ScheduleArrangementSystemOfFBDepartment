package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.major.MajorType;

/**
 * Created by bytenoob on 15/11/1.
 */
public interface MajorTypeDao extends PagingAndSortingRepository<MajorType,Integer> {
    MajorType findByMajorCodeEquals(String majorCode);
    MajorType findByTitleEquals(String title);
    MajorType findByMajorCodeAndTitle(String majorCode , String title);
}
