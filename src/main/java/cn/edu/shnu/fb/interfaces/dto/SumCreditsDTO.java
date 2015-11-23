package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/23.
 */
public class SumCreditsDTO {
    private String sumpoc;
    private String summoc;
    private String sumrec;
    private String sumlic;
    private String sumlec;
    private String sumfec;
    private String sumtc;
    private String sumgpc;
    private String isumpoc;//i表示是学位课程
    private String isummoc;
    private String isumrec;
    private String isumlic;
    private String isumlec;
    private String isumfec;
    private String isumtc;
    private String isumgpc;
    private String asum;
    private String bsum;

    public String getSumpoc(){return  sumpoc;}
    public String getSummoc(){return summoc;}
    public String getSumrec(){return sumrec;}
    public String getSumlic(){return sumlic;}
    public String getSumlec(){return sumlec;}
    public String getSumfec(){return sumfec;}
    public String getSumtc(){return sumtc;}
    public String getSumgpc(){return sumgpc;}

    public String getIsumpoc(){return isumpoc;}
    public String getIsummoc(){return isummoc;}
    public String getIsumrec(){return isumrec;}
    public String getIsumlic(){return isumlic;}
    public String getIsumlec(){return isumlec;}
    public String getIsumfec(){return isumfec;}
    public String getIsumtc(){return isumtc;}
    public String getIsumgpc(){return isumgpc;}

    public String getAsum(){return asum;}
    public String getBsum(){return bsum;}


    public SumCreditsDTO(String sumpoc,String summoc,String sumrec,String sumlic,String sumlec,String sumfec,String sumtc,String sumgpc,
                         String  isumpoc,String isummoc,String isumrec,String isumlic,String isumlec,String isumfec,String isumtc,String isumgpc,
                         String asum,String bsum){
        this.sumpoc=sumpoc;
        this.summoc=summoc;
        this.sumrec=sumrec;
        this.sumlic=sumlic;
        this.sumlec=sumlec;
        this.sumfec=sumfec;
        this.sumtc=sumtc;
        this.sumgpc=sumgpc;

        this.isumpoc=isumpoc;
        this.isummoc=isummoc;
        this.isumrec=isumrec;
        this.isumlic=isumlic;
        this.isumlec=isumlec;
        this.isumfec=isumfec;
        this.isumtc=isumtc;
        this.isumgpc=isumgpc;

        this.asum=asum;
        this.bsum=bsum;
    }
}
