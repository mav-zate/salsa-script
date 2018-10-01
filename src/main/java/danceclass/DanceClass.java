package danceclass;

public class DanceClass {
    private String className;
    private String level;
    private String instructorName;
    private Integer weekNumber;

    DanceClass(String className, String level, String instructorName, Integer weekNumber) {
        this.className = className;
        this.level = level;
        this.instructorName = instructorName;
        this.weekNumber = weekNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
}
