package cn.edu.shnu.fb.domain.mergedClass;


        import java.io.Serializable;
        import javax.persistence.*;
        import java.util.List;

        import com.fasterxml.jackson.annotation.JsonIgnore;

        import cn.edu.shnu.fb.domain.Imp.Imp;

/**
 * The persistent class for the merged_class database table.
 *
 */
@Entity
@Table(name="merged_class")
public class MergedClass implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;

    private int status;

    private float periodWeeks;

    private float periodHours;


    //bi-directional many-to-one association to Imp
    @JsonIgnore
    @OneToMany(mappedBy="mergedClass")
    private List<Imp> imps;

    public MergedClass() {
        this.status = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Imp> getImps() {
        return this.imps;
    }

    public void setImps(List<Imp> imps) {
        this.imps = imps;
    }

    public Imp addImp(Imp imp) {
        getImps().add(imp);
        imp.setMergedClass(this);

        return imp;
    }

    public Imp removeImp(Imp imp) {
        getImps().remove(imp);
        imp.setMergedClass(null);

        return imp;
    }

    public float getPeriodWeeks() {
        return periodWeeks;
    }

    public void setPeriodWeeks(final float periodWeeks) {
        this.periodWeeks = periodWeeks;
    }

    public float getPeriodHours() {
        return periodHours;
    }

    public void setPeriodHours(final float periodHours) {
        this.periodHours = periodHours;
    }

    public void loadImps(){
        imps.size();
    }
}