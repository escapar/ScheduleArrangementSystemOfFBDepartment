package cn.edu.shnu.fb.domain.term;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface TermRepository extends PagingAndSortingRepository<Term,Integer>{
    Term findTermByYearAndPart(Integer year,Integer part);
}
