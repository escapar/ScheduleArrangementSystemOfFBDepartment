package cn.edu.shnu.fb.interfaces.dto;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by bytenoob on 15/12/13.
 */
public class FrontEndSalaryDTO {
    List<SalaryDTO> salaryDTOs;
    List<SalaryDTO> salaryAdjustments;
    public FrontEndSalaryDTO(){

    }
    public List<SalaryDTO> getSalaryDTOs() {
        return salaryDTOs;
    }

    public void setSalaryDTOs(final List<SalaryDTO> salaryDTOs) {
        this.salaryDTOs = salaryDTOs;
    }

    public List<SalaryDTO> getSalaryAdjustments() {
        return salaryAdjustments;
    }

    public void setSalaryAdjustments(final List<SalaryDTO> salaryAdjustments) {
        this.salaryAdjustments = salaryAdjustments;
    }
}
