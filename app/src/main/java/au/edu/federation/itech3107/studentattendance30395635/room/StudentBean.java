package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Student chart
@Entity
public class StudentBean implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Long number;
    private int classId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", classId=" + classId +
                '}';
    }
}

