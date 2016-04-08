package cn.edu.shnu.fb.interfaces.dto;

import java.util.List;

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
    String tc="";
    String tn="";
    String tt="";
    String comment;
    public ImpExcelGridDTO(String code, String title, String exam, float credits, float weeks, float hours, String deg, List<String> tcs, List<String> tns, List<String> tts, String comment){
        this.code = code;
        this.title = title;
        this.exam = exam;
        this.credits = credits;
        this.weeks = weeks;
        this.hours = hours;
        this.deg = deg;
        if(tcs!=null) {
            for (String tc : tcs) {
                if (!this.tc.isEmpty()) {
                    this.tc += (',' + tc);
                } else {
                    this.tc = tc;
                }
            }
        }
        if(tns!=null) {
            for (String tn : tns) {
                if (!this.tn.isEmpty()) {
                    this.tn += (',' + tn);
                } else {
                    this.tn = tn;
                }
            }
        }
        if(tts!=null) {
            for (String tt : tts) {
                if (!this.tt.isEmpty()) {
                    this.tt += (',' + tt);
                } else {
                    this.tt = tt;
                }
            }
        }
        this.comment = comment;
    }
}
