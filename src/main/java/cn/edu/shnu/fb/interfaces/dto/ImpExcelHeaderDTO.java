package cn.edu.shnu.fb.interfaces.dto;

/**
 * Created by bytenoob on 15/11/10.
 */
public class ImpExcelHeaderDTO {

    private float planPOC; //Public Oblige

    private float planMOC; //Major Oblige

    private float planREC; //Restricted Electable

    private float planFEC; //Free Electable

    private float planTC;  //Training

    private float planSumC;

    private float impPOC;

    private float impMOC;

    private float impREC;

    private float impFEC;

    private float impTC;

    private float impSumC;


    public float getPlanPOC() {
        return planPOC;
    }

    public float getPlanMOC() {
        return planMOC;
    }

    public float getPlanREC() {
        return planREC;
    }

    public float getPlanFEC() {
        return planFEC;
    }

    public float getPlanTC() {
        return planTC;
    }

    public float getPlanSumC() {
        return planSumC;
    }

    public float getImpPOC() {
        return impPOC;
    }

    public float getImpMOC() {
        return impMOC;
    }

    public float getImpREC() {
        return impREC;
    }

    public float getImpFEC() {
        return impFEC;
    }

    public float getImpTC() {
        return impTC;
    }

    public float getImpSumC() {
        return impSumC;
    }

    public ImpExcelHeaderDTO(float planpoc, float planmoc, float planrec, float planfec, float plantc, float imppoc, float impmoc, float imprec, float impfec, float imptc) {
        this.planPOC=planpoc;
        this.planMOC=planmoc;
        this.planREC=planrec;
        this.planFEC=planfec;
        this.planTC=plantc;
        this.planSumC = planpoc + planmoc + planrec + planfec + plantc ;
        this.impPOC=imppoc;
        this.impMOC=impmoc;
        this.impREC=imprec;
        this.impFEC=impfec;
        this.impTC=imptc;
        this.impSumC = imppoc + impmoc + imprec + impfec + imptc ;
    }

}