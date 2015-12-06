package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.mergedClass.MergedClass;

/**
 * Created by bytenoob on 15/12/4.
 */
public interface MergedClassDao  extends PagingAndSortingRepository<MergedClass,Integer> {
    List<MergedClass> findByStatus(Integer status);

}
