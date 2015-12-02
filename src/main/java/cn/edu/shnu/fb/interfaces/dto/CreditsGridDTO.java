package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by bytenoob on 15/12/2.
 */
public class CreditsGridDTO {
    private String type;//每个学期的学分

    public String getPoc() {
        return poc;
    }

    public void setPoc(final String poc) {
        this.poc = poc;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getMoc() {
        return moc;
    }

    public void setMoc(final String moc) {
        this.moc = moc;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(final String rec) {
        this.rec = rec;
    }

    public String getLic() {
        return lic;
    }

    public void setLic(final String lic) {
        this.lic = lic;
    }

    public String getLec() {
        return lec;
    }

    public void setLec(final String lec) {
        this.lec = lec;
    }

    public String getFec() {
        return fec;
    }

    public void setFec(final String fec) {
        this.fec = fec;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(final String tc) {
        this.tc = tc;
    }

    public String getGpc() {
        return gpc;
    }

    public void setGpc(final String gpc) {
        this.gpc = gpc;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(final String sum) {
        this.sum = sum;
    }

    private String poc;//每个学期的学分
    private String moc;
    private String rec;
    private String lic;
    private String lec;
    private String fec;
    private String tc;
    private String gpc;
    private String sum;
    public CreditsGridDTO(CreditsDTO c,Integer type){
        if(type == 1){
            this.type = "学分";
            this.poc = c.getPoc();
            this.moc = c.getMoc();
            this.rec = c.getRec();
            this.lic = c.getLic();
            this.lec = c.getLec();
            this.fec = c.getFec();
            this.tc = c.getTc();
            this.gpc=c.getGpc();
            this.sum = c.getSum();
        }else{
            this.type = "学时";
            this.poc = c.getIpoc();
            this.moc = c.getImoc();
            this.rec = c.getIrec();
            this.lic = c.getIlic();
            this.lec = c.getIlec();
            this.fec = c.getIfec();
            this.tc = c.getItc();
            this.gpc=c.getIgpc();
            this.sum = c.getIsum();
        }
    }
}
