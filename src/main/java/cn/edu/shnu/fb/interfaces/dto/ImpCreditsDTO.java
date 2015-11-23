package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by zhouziyi on 15/11/21.
 */
public class ImpCreditsDTO {
    CreditsHeaderDTO headerdto;
    CreditsDTO fircredto;
    CreditsDTO seccredto;
    CreditsDTO thicredto;
    CreditsDTO foucredto;
    CreditsDTO fivcredto;
    CreditsDTO sixcredto;
    CreditsDTO sevcredto;
    CreditsDTO eigcredto;
    SumCreditsDTO sumcreditsdto;

    public CreditsHeaderDTO getHeaderdto(){return headerdto;}
    public void setHeaderdto(final CreditsHeaderDTO headerdto){this.headerdto=headerdto;}

    public CreditsDTO getFircredto(){return fircredto;}
    public void setFircredto(final CreditsDTO fircredto) {this.fircredto=fircredto;}

    public CreditsDTO getSeccredto(){return seccredto;}
    public void setSeccredto(final CreditsDTO seccredto) {this.seccredto=seccredto;}

    public CreditsDTO getThicredto(){return thicredto;}
    public void setThicredto(final CreditsDTO thicredto) {this.thicredto=thicredto;}

    public CreditsDTO getFoucredto(){return foucredto;}
    public void setFoucredto(final CreditsDTO foucredto) {this.foucredto=foucredto;}

    public CreditsDTO getFivcredto(){return fivcredto;}
    public void setFivcredto(final CreditsDTO fivcredto) {this.fivcredto=fivcredto;}

    public CreditsDTO getSixcredto(){return sixcredto;}
    public void setSixcredto(final CreditsDTO sixcredto) {this.sixcredto=sixcredto;}

    public CreditsDTO getSevcredto(){return sevcredto;}
    public void setSevcredto(final CreditsDTO sevcredto) {this.sevcredto=sevcredto;}

    public CreditsDTO getEigcredto(){return eigcredto;}
    public void setEigcredto(final CreditsDTO eigcredto) {this.eigcredto=eigcredto;}

    public SumCreditsDTO getSumcreditsdto(){return sumcreditsdto;}
    public void setSumcreditsdto(final SumCreditsDTO sumcreditsdto){this.sumcreditsdto=sumcreditsdto;}

    public ImpCreditsDTO(CreditsHeaderDTO headerdto,CreditsDTO fircredto,CreditsDTO seccredto,CreditsDTO thicredto,CreditsDTO foucredto,CreditsDTO fivcredto,CreditsDTO sixcredto,CreditsDTO sevcredto,CreditsDTO eigcredto,SumCreditsDTO sumcreditsdto){

        this.headerdto=headerdto;
        this.fircredto=fircredto;
        this.seccredto=seccredto;
        this.thicredto=thicredto;
        this.foucredto=foucredto;
        this.fivcredto=fivcredto;
        this.sixcredto=sixcredto;
        this.sevcredto=sevcredto;
        this.eigcredto=eigcredto;
        this.sumcreditsdto=sumcreditsdto;

    }

}
