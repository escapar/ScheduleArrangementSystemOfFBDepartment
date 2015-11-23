package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/21.
 */
public class CreditsDTO {

    private String poc;//每个学期的学分
    private String moc;
    private String rec;
    private String lic;
    private String lec;
    private String fec;
    private String tc;
    private String gpc;
    private String ipoc;//每个学期的学位课学分
    private String imoc;
    private String irec;
    private String ilic;
    private String ilec;
    private String ifec;
    private String itc;
    private String igpc;
    private String sum;
    private String isum;


    public String getPoc() {
        return poc;
    }

    public String getMoc() {return  moc;}

    public String getRec() {
        return rec;
    }

    public String getLic() {
        return lic;
    }

    public String getLec() {return lec;}

    public String getFec() {
        return fec;
    }

    public String getTc() { return tc;}

    public String getGpc() {return  gpc;}
    public String getIpoc() {
        return ipoc;
    }

    public String getImoc() {return  imoc;}

    public String getIrec() {
        return irec;
    }

    public String getIlic() {
        return ilic;
    }

    public String getIlec() {return ilec;}

    public String getIfec() {
        return ifec;
    }

    public String getItc() { return itc;}

    public String getIgpc() {return  igpc;}

    public String getSum(){return sum;}

    public String getIsum(){return isum;}



    public CreditsDTO(String poc, String moc, String rec, String lic, String lec, String fec, String tc, String gpc,
                      String ipoc, String imoc, String irec, String ilic, String ilec, String ifec, String itc, String igpc,
                      String sum,String isum)
    {
        this.poc=poc;
        this.moc=moc;
        this.rec=rec;
        this.lic=lic;
        this.lec=lec;
        this.fec=fec;
        this.tc=tc;
        this.gpc=gpc;
        this.ipoc=ipoc;
        this.imoc=imoc;
        this.irec=irec;
        this.ilic=ilic;
        this.ilec=ilec;
        this.ifec=ifec;
        this.itc=itc;
        this.igpc=igpc;
        this.sum=sum;
        this.isum=isum;


    }


}
