package cn.edu.shnu.fb.infrastructure.persistence;

import cn.edu.shnu.fb.domain.Imp.ImpComment;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by zhouziyi on 15/11/18.
 */
public interface ImpCommentDao extends PagingAndSortingRepository<ImpComment,Integer>{
    ImpComment findImpCommentByMajorIdAndTermId(Integer majorId, Integer termId);
    ImpComment findByTermAndMajor(Term term,Major major);
}
