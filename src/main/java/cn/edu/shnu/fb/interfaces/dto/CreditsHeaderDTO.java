package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/22.
 */
public class CreditsHeaderDTO {
    private int grade;
    private String code;
    private String title;

    public int getGrade() {
        return grade;
    }
    public String getCode() {
        return code;
    }

    public String  getTitle(){return title;}

    public CreditsHeaderDTO(int grade,String code,String title){
        this.grade=grade;
        this.code=code;
        this.title=title;
    }
}
