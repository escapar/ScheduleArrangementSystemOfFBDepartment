package cn.edu.shnu.fb.infrastructure.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.Imp.ImpComment;
import cn.edu.shnu.fb.domain.common.CourseType;
import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

/**
 * Created by bytenoob on 15/11/22.
 */
public interface ImpCommentDao extends PagingAndSortingRepository<ImpComment,Integer> {
    ImpComment findByTermAndMajor(Term term,Major major);
}
