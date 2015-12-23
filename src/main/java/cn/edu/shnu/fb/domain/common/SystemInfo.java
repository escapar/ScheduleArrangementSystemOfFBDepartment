package cn.edu.shnu.fb.domain.common;

        import java.io.Serializable;
        import javax.persistence.*;
        import java.util.Date;


/**
 * The persistent class for the system_info database table.
 *
 */
@Entity
@Table(name="system_info")
public class SystemInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer year;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    public SystemInfo() {
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

}