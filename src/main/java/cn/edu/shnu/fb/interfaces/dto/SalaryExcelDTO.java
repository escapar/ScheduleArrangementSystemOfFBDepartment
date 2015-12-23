package cn.edu.shnu.fb.interfaces.dto;

import cn.edu.shnu.fb.domain.Imp.Salary;

import java.util.List;

/**
 * Created by zhouziyi on 15/12/19.
 */
public class SalaryExcelDTO {

    List<SalaryDTO> salaryDTOs;
    SalaryHeaderDTO salaryHeaderDTO;
    public SalaryExcelDTO(List<SalaryDTO> salaryDTOs,SalaryHeaderDTO salaryHeaderDTO){
        this.salaryDTOs=salaryDTOs;
        this.salaryHeaderDTO=salaryHeaderDTO;
    }

    public List<SalaryDTO> getSalaryDTOs() {
        return salaryDTOs;
    }

    public void setSalaryDTOs(final List<SalaryDTO> salaryDTOs) {
        this.salaryDTOs = salaryDTOs;
    }

    public SalaryHeaderDTO getSalaryHeaderDTO() {
        return salaryHeaderDTO;
    }

    public void setSalaryHeaderDTO(final SalaryHeaderDTO salaryHeaderDTO) {
        this.salaryHeaderDTO = salaryHeaderDTO;
    }
}
