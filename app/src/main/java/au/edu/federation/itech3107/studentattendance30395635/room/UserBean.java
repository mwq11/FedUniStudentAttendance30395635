package au.edu.federation.itech3107.studentattendance30395635.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
//User table
@Entity
public class UserBean{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String pwd;
    private String nick;
    private String mobile;
    private String header;
    private int authon;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getAuthon() {
        return authon;
    }

    public void setAuthon(int authon) {
        this.authon = authon;
    }
}

