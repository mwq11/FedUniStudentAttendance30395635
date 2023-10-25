package au.edu.federation.itech3107.studentattendance30395635.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class CourseAncestor implements Serializable {

    public static final int SHOW_ALL = 0;
    public static final int SHOW_DOUBLE = 1;
    public static final int SHOW_SINGLE = 2;
    public int startIndex;
    public int endIndex;
    /**
     * Odd-even display
     */
    public int showIndex = SHOW_ALL;


    /**
     * Line number
     */
    public int row;
    /**
     * The number of rows occupied
     */
    public int rowNum = 1;
    /**
     * Column number
     */
    public int col;

    /**
     * colour
     */
    public int color = -1;
    public int color2 = -1;
    /**
     * Displayed content
     */
    public String text;

    /**
     * Active state
     */
    public boolean activeStatus = true;

    /**
     * Show or not
     */
    public boolean displayable = true;


    public String showIndexes;

    public CourseAncestor(int row, int rowNum, int col, int color) {
        this.row = row;
        this.rowNum = rowNum;
        this.col = col;
        this.color = color;
    }

    public CourseAncestor() {
    }

    public void init(int row, int col, int rowNum, int color) {
        this.row = row;
        this.rowNum = rowNum;
        this.col = col;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public CourseAncestor setRow(int row) {
        this.row = row;
        return this;
    }

    public int getRowNum() {
        return rowNum;
    }

    public CourseAncestor setRowNum(int rowNum) {
        this.rowNum = rowNum;
        return this;
    }

    public int getCol() {
        return col;
    }

    public CourseAncestor setCol(int col) {
        this.col = col;
        return this;
    }

    public int getColor() {
        return color;
    }

    public CourseAncestor setColor(int color) {
        this.color = color;
        return this;
    }

    public String getText() {
        return text;
    }

    public CourseAncestor setText(String text) {
        this.text = text;
        return this;
    }

    public boolean getActiveStatus() {
        return activeStatus;
    }

    public CourseAncestor setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public CourseAncestor setDisplayable(boolean displayable) {
        this.displayable = displayable;
        return this;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public CourseAncestor setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public CourseAncestor setEndIndex(int endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    public int getShowType() {
        return showIndex;
    }

    public CourseAncestor setShowType(int showIndex) {
        this.showIndex = showIndex;
        return this;
    }

    public List<Integer> getShowIndexes() {
        if (showIndexes == null || "".equals(showIndexes)) {
            return null;
        }
        String[] split = showIndexes.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (showIndexes != null && !"".equals(showIndexes)) {
                list.add(Integer.parseInt(split[i]));
            }
        }
        return list;
    }

    public CourseAncestor addIndex(int index) {
        if (!showIndexes.contains(index + ",")) {
            if (showIndexes == null || "".equals(showIndexes)) {
                return null;
            }
            String[] split = showIndexes.split(",");
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (showIndexes != null && !"".equals(showIndexes)) {
                    list.add(Integer.parseInt(split[i]));
                }
            }
            list.add(index);
            showIndexes = list.toString();
        }
        return this;
    }

    public boolean shouldShow(int index) {
        if (showIndexes == null || "".equals(showIndexes)) {
            return false;
        }
        String[] split = showIndexes.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (showIndexes != null && !"".equals(showIndexes)) {
                list.add(Integer.parseInt(split[i]));
            }
        }
        return list.contains(index);
    }

    @Override
    public String toString() {
        return "CourseAncestor{" +
                "row=" + row +
                ", rowNum=" + rowNum +
                ", col=" + col +
                ", color=" + color +
                ", text='" + text + '\'' +
                ", activeStatus=" + activeStatus +
                ", displayable=" + displayable +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
