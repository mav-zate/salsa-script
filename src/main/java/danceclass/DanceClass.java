package danceclass;

public class DanceClass {
    private String className;
    private String level;
    private String instructorName;
    private Integer weekNumber;
    private ClassTime startTime;
    private ClassTime endTime;

    DanceClass() {
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

    public ClassTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ClassTime startTime) {
        this.startTime = startTime;
    }

    public ClassTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ClassTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(DanceClassUtil.CLASS_NAME).append(": ").append(className).append("\n")
                .append(level).append("\n")
                .append(DanceClassUtil.CLASS_INSTRUCTOR).append(": ").append(instructorName).append("\n")
                .append(startTime).append(" - ").append(endTime).append("\n");
//                .append(DanceClassUtil.CLASS_DATE_RANGE).append(": ").append(weekNumber).append("\n");

        return sb.toString();
    }
}
