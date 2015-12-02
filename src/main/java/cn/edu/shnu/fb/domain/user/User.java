package cn.edu.shnu.fb.domain.user;

/**
 * Created by bytenoob on 15/12/2.
 */

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user database table.
 *
 */
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String password;

    private int role;

    private String username;

    //bi-directional many-to-one association to Teacher
    @ManyToOne
    private Teacher teacher;

    public User() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return this.role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}