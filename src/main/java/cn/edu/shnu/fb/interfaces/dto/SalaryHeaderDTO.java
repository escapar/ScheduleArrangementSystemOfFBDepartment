package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/12/20.
 */
public class SalaryHeaderDTO {

    private int fromyear;
    private int toyear;
    private int part;

    public int getFromyear() {
        return fromyear;
    }
    public void setFromyear(final int fromyear) {
        this.fromyear = fromyear;
    }
    public int getToyear() {
        return toyear;
    }

    public void setToyear(final int toyear) {
        this.toyear = toyear;
    }
    public int getPart() {
        return part;
    }

    public void setPart(final int part) {
        this.part = part;
    }

    public SalaryHeaderDTO(int fromyear,int part){
        this.fromyear=fromyear;
        this.toyear=fromyear+1;
        this.part=part;
    }
}
