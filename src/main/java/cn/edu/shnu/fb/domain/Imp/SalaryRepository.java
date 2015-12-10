package cn.edu.shnu.fb.domain.Imp;

import org.springframework.beans.factory.annotation.Autowired;

import cn.edu.shnu.fb.infrastructure.persistence.SalaryDao;
import cn.edu.shnu.fb.interfaces.dto.SalaryDTO;

/**
 * Created by bytenoob on 15/12/8.
 */
public class SalaryRepository {
    @Autowired
    SalaryDao salaryDao;

    public void persistSalary(Integer impId , SalaryDTO salaryDTO){
        Salary salary = new Salary(impId,salaryDTO);
        salaryDao.save(salary);
    }


}
