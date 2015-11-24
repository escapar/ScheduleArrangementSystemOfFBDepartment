package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/17.
 */
public class MajAndTmAndImpComDTO {
    private String majorTitle;
    private String majorCode;
    private int termCount;
    private String impComment;


    public String getMajorCode() { return majorCode; }

    public String getImpComment() { return impComment; }

    public int getTermCount() { return termCount; }
    public String getMajorTitle() {
        return majorTitle;
    }

    public void setMajorTitle(final String majorTitle) {
        this.majorTitle = majorTitle;
    }

    public MajAndTmAndImpComDTO(String majorTitle,String majorCode, int termCount,String impComment){
        this.majorTitle = majorTitle;
        this.majorCode=majorCode;
        this.impComment=impComment;
        this.termCount=termCount;
    }

}
