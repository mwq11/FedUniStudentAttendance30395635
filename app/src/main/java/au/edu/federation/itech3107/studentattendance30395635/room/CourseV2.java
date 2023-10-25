package au.edu.federation.itech3107.studentattendance30395635.room;

import android.text.TextUtils;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395635.bean.CourseAncestor;

//Curriculum schedule
@Entity
public class CourseV2 extends CourseAncestor implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int couId;

    private String couName;
    private String couLocation;
    private String couTeacher;

    private Integer couWeek;

    private Integer couStartNode;
    private Integer couNodeCount;

    private String couAllWeek;

    private Integer couColor;

    private Long couCgId;

    private String couOnlyId;
    
    private Boolean couDeleted = false;
    private int groupId;
    private String joinClassId;
    private String checkInStudentIds;

    public String getJoinClassId() {
        return joinClassId;
    }

    public void setJoinClassId(String joinClassId) {
        this.joinClassId = joinClassId;
    }

    public String getCheckInStudentIds() {
        return checkInStudentIds;
    }

    public void setCheckInStudentIds(String checkInStudentIds) {
        this.checkInStudentIds = checkInStudentIds;
    }

    public CourseV2 init() {
        boolean b = getCouWeek() != null;
        if(getCouWeek()!=null){
            setRow(getCouWeek());
        }

        if (getCouColor() != null) {
            setColor(getCouColor());
        }

        List<Integer> showIndexes1 = getShowIndexes();
        if (showIndexes1 != null) {
            showIndexes1.clear();
        }

        try {
            String[] split = couAllWeek.split(",");
            for (String s : split) {
                addIndex(Integer.parseInt(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getCouNodeCount()!=null && getCouNodeCount() != 0) {
            setCol(getCouStartNode());
            setRowNum(getCouNodeCount());
        } else {
        }

        if (TextUtils.isEmpty(getCouLocation())) {
            setText(getCouName());
        } else {
            setText(getCouName() + "\n@" + getCouLocation());
        }

        return this;
    }

    /**
     * name teacher location 相等判为同一门课
     */
    public boolean isSameClass(CourseV2 other) {
        if (other == null) {
            return false;
        }

        if (other.getCouName() == null || this.getCouName() == null) {
            return false;
        }

        if (other.getCouName().equals(this.getCouName())) {
            if ((other.getCouTeacher() == null && this.getCouTeacher() == null)
                    || (other.getCouTeacher().equals(this.getCouTeacher()))) {
                return (other.getCouLocation() == null && this.getCouLocation() == null)
                        || (other.getCouLocation().equals(this.getCouLocation()));
            }
        }
        return false;
    }

    /**
     * name teacher 相等判为同一门课
     */
    public boolean isSameClassWithoutLocation(CourseV2 other) {
        if (other == null) {
            return false;
        }

        if (other.getCouName() == null || this.getCouName() == null) {
            return false;
        }

        if (other.getCouName().equals(this.getCouName())) {
            return (other.getCouTeacher() == null && this.getCouTeacher() == null)
                    || (other.getCouTeacher().equals(this.getCouTeacher()));
        }
        return false;
    }

    /**
     * 判断onlyId相等
     */
    public boolean onlyIdEqualsOnlyIdOf(CourseV2 CourseV2) {
        if (CourseV2 == null) {
            return false;
        }

        if (this.getCouOnlyId() == null || CourseV2.getCouOnlyId() == null) {
            return false;
        }
        return this.getCouOnlyId().equals(CourseV2.getCouOnlyId());
    }

    public int getCouId() {
        return couId;
    }

    public void setCouId(int couId) {
        this.couId = couId;
    }

    public CourseV2 setCouIdReturn(int couId) {
        this.couId = couId;
        return this;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouLocation() {
        return couLocation;
    }

    public void setCouLocation(String couLocation) {
        this.couLocation = couLocation;
    }

    public String getCouTeacher() {
        return couTeacher;
    }

    public void setCouTeacher(String couTeacher) {
        this.couTeacher = couTeacher;
    }

    public Integer getCouWeek() {
        return couWeek;
    }

    public void setCouWeek(Integer couWeek) {
        this.couWeek = couWeek;
    }

    public CourseV2 setCouWeekR(Integer couWeek) {
        this.couWeek = couWeek;
        return this;
    }

    public Integer getCouStartNode() {
        return couStartNode;
    }

    public void setCouStartNode(Integer couStartNode) {
        this.couStartNode = couStartNode;
    }

    public CourseV2 setCouStartNodeR(Integer couStartNode) {
        this.couStartNode = couStartNode;
        return this;
    }


    public Integer getCouNodeCount() {
        return couNodeCount;
    }

    public void setCouNodeCount(Integer couNodeCount) {
        this.couNodeCount = couNodeCount;
    }

    public CourseV2 setCouNodeCountR(Integer couNodeCount) {
        this.couNodeCount = couNodeCount;
        return this;
    }

    public String getCouAllWeek() {
        return couAllWeek;
    }

    public void setCouAllWeek(String couAllWeek) {
        this.couAllWeek = couAllWeek;
    }

    public CourseV2 setCouAllWeekR(String couAllWeek) {
        this.couAllWeek = couAllWeek;
        return this;
    }

    public Integer getCouColor() {
        return couColor;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setCouColor(Integer couColor) {
        this.couColor = couColor;
    }

    public CourseV2 setCouColorR(Integer couColor) {
        this.couColor = couColor;
        return this;
    }

    public Long getCouCgId() {
        return couCgId;
    }

    public void setCouCgId(Long couCgId) {
        this.couCgId = couCgId;
    }

    public CourseV2 setCouCgIdR(Long couCgId) {
        this.couCgId = couCgId;
        return this;
    }

    @Override
    public String toString() {
        return "CourseV2{" +
                "couId=" + couId +
                ", couName='" + couName + '\'' +
                ", couLocation='" + couLocation + '\'' +
                ", couTeacher='" + couTeacher + '\'' +
                ", couWeek=" + couWeek +
                ", couStartNode=" + couStartNode +
                ", couNodeCount=" + couNodeCount +
                ", couAllWeek='" + couAllWeek + '\'' +
                ", couColor=" + couColor +
                ", couCgId=" + couCgId +
                '}';
    }

    public String toSuperString() {
        return super.toString();
    }

    public String getCouOnlyId() {
        return this.couOnlyId;
    }

    public void setCouOnlyId(String couOnlyId) {
        this.couOnlyId = couOnlyId;
    }

    public CourseV2 setCouOnlyIdR(String couOnlyId) {
        this.couOnlyId = couOnlyId;
        return this;
    }

    public Boolean getCouDeleted() {
        return this.couDeleted;
    }

    public void setCouDeleted(Boolean couDeleted) {
        this.couDeleted = couDeleted;
    }
}
