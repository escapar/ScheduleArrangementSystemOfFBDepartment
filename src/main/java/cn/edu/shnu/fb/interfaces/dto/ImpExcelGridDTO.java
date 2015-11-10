package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by bytenoob on 15/11/10.
 */
public class ImpExcelGridDTO {
    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getExam() {
        return exam;
    }

    public float getCredits() {
        return credits;
    }

    public float getWeeks() {
        return weeks;
    }

    public float getHours() {
        return hours;
    }

    public String getDeg() {
        return deg;
    }

    public String getTc() {
        return tc;
    }

    public String getTn() {
        return tn;
    }

    public String getTt() {
        return tt;
    }

    public String getComment() {
        return comment;
    }

    String code;
    String title;
    String exam;
    float credits;
    float weeks;
    float hours;
    String deg;
    String tc;
    String tn;
    String tt;
    String comment;
    public ImpExcelGridDTO(String code, String title, String exam, float credits, float weeks, float hours, String deg, String tc, String tn, String tt, String comment){
        this.code = code;
        this.title = title;
        this.exam = exam;
        this.credits = credits;
        this.weeks = weeks;
        this.hours = hours;
        this.deg = deg;
        this.tc = tc;
        this.tn = tn;
        this.tt = tt;
        this.comment = comment;
    }
}
