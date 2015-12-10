package cn.edu.shnu.fb.infrastructure.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.Imp.Salary;

/**
 * Created by bytenoob on 15/12/8.
 */
public interface SalaryDao extends PagingAndSortingRepository<Salary,Integer> {

}
