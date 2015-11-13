package cn.edu.shnu.fb.interfaces.dto;

import java.util.List;

/**
 * Created by bytenoob on 15/11/10.
 */
public class ImpExcelDTO {

    ImpExcelHeaderDTO header;
    List<ImpExcelGridDTO> podtos;
    List<ImpExcelGridDTO> modtos;
    List<ImpExcelGridDTO> redtos;
    List<ImpExcelGridDTO> fedtos;
    List<ImpExcelGridDTO> tdtos;


    public ImpExcelHeaderDTO getHeader() {
        return header;
    }

    public List<ImpExcelGridDTO> getPodto() {
        return podtos;
    }

    public void setHeader(final ImpExcelHeaderDTO header) {
        this.header = header;
    }

    public List<ImpExcelGridDTO> getPodtos() {
        return podtos;
    }

    public void setPodtos(final List<ImpExcelGridDTO> podtos) {
        this.podtos = podtos;
    }

    public List<ImpExcelGridDTO> getModtos() {
        return modtos;
    }

    public void setModtos(final List<ImpExcelGridDTO> modtos) {
        this.modtos = modtos;
    }

    public List<ImpExcelGridDTO> getRedtos() {
        return redtos;
    }

    public void setRedtos(final List<ImpExcelGridDTO> redtos) {
        this.redtos = redtos;
    }

    public List<ImpExcelGridDTO> getFedtos() {
        return fedtos;
    }

    public void setFedtos(final List<ImpExcelGridDTO> fedtos) {
        this.fedtos = fedtos;
    }

    public List<ImpExcelGridDTO> getTdtos() {
        return tdtos;
    }

    public void setTdtos(final List<ImpExcelGridDTO> tdtos) {
        this.tdtos = tdtos;
    }

    public ImpExcelDTO(ImpExcelHeaderDTO header , List<ImpExcelGridDTO> podtos)
    {
        this.header = header;
        this.podtos = podtos;
        this.modtos = modtos;
        this.redtos = redtos;
        this.fedtos = fedtos;
        this.tdtos = tdtos;
    }
}
