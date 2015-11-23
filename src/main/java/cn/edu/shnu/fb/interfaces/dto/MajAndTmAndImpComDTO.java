package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/17.
 */
public class MajAndTmAndImpComDTO {
    private String majorCode;
    private int termCount;
    private String impComment;


    public String getMajorCode() { return majorCode; }

    public String getImpComment() { return impComment; }

    public int getTermCount() { return termCount; }

    public MajAndTmAndImpComDTO(String majorCode, int termCount,String impComment){
        this.majorCode=majorCode;
        this.impComment=impComment;
        this.termCount=termCount;
    }

}
