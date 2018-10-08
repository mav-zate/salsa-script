package danceclass;

public class ClassTime {
    private Integer hour;
    private Integer minutes;

    ClassTime() {

    }

    public ClassTime(Integer hour, Integer minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public void addHour() {
        hour = (hour + 1) % 12;
    }

    public void addHalfHour() {
        if (minutes == 30) {
            addHour();
        }

        minutes = (minutes + 30) % 60;
    }

    @Override
    public String toString() {
        String minuteString = minutes.toString();
        if (minutes < 10) {
            minuteString = "0" + minuteString;
        }
        return String.format("%s:%s", hour, minuteString);
    }
}
