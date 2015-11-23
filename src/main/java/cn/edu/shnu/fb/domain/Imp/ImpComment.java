package cn.edu.shnu.fb.domain.Imp;

/**
 * Created by zhouziyi on 15/11/18.
 */


import cn.edu.shnu.fb.domain.major.Major;
import cn.edu.shnu.fb.domain.term.Term;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the imp_comment database table.
 *
 */
@Entity
@Table(name="imp_comment")
public class ImpComment  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Major major;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Term term;

    @Column(name = "comment")
    private String comment;

    public ImpComment() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Major getMajor() {
        return this.major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(final Term term) {
        this.term = term;
    }

}
