package cn.edu.shnu.fb.infrastructure.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/2.
 */
public interface TermDao extends PagingAndSortingRepository<Term,Integer>{
    Term findTermByYearAndPart(Integer year,Integer part);
}
