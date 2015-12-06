package cn.edu.shnu.fb.domain.major;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the major_type database table.
 *
 */
@Entity
@Table(name="major_type")
public class MajorType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="major_code")
    private String majorCode;

    private String title;


    public MajorType() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMajorCode() {
        return this.majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}