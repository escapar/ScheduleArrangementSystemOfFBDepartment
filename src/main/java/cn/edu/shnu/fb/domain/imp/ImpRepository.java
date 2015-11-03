package cn.edu.shnu.fb.domain.imp;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface ImpRepository extends PagingAndSortingRepository<Imp,Integer> {
    Imp findImpByMajorAndTerm(Major major,Term term);
}
