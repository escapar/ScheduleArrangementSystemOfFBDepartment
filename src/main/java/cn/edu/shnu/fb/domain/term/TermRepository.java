package cn.edu.shnu.fb.domain.term;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.infrastructure.persistence.TermDao;

/**
 * Created by bytenoob on 15/11/7.
 */
@Repository
public class TermRepository {
    @Autowired
    TermDao termDao;

    public Term findTermByGradeAndTermCount(int grade , int termCount){
        int termYear = grade + (termCount - 1) / 2 ;
        int termPart = termCount % 2 == 0 ? 2 : 1;
        return termDao.findTermByYearAndPart(termYear,termPart);
    }
    public Term createTermByGradeAndTermCount(int grade , int termCount){
        int termYear = grade + (termCount - 1) / 2 ;
        int termPart = termCount % 2 == 0 ? 2 : 1;
        Term term = new Term();
        term.setYear(termYear);
        term.setPart(termPart);
        term.setWeeks(16);
        return termDao.save(term);
    }
    public Term findTermById(int termId){
        return termDao.findOne(termId);
    }
    public Term findTermByYearAndPart(int termYear , int termPart){
        return termDao.findTermByYearAndPart(termYear,termPart);
    }
    public void save(Term term){
        termDao.save(term);
    }
}
