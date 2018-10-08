package danceclass;

public class ClassTimeUtil {

    public static ClassTime getClassEndTime(ClassTime classStartTime, Integer colSpanValue) throws AbnormalClassLength {
        if (colSpanValue < 2 || colSpanValue > 3) {
            throw new AbnormalClassLength();
        }

        ClassTime classEndTime = clone(classStartTime);

        if (colSpanValue == 2) {
            classEndTime.addHour();
        } else if (colSpanValue == 3) {
            classEndTime.addHour();
            classEndTime.addHalfHour();
        }

        return classEndTime;
    }

    private static ClassTime clone(ClassTime original) {
        Integer hour = original.getHour();
        Integer minutes = original.getMinutes();

        return new ClassTime(hour, minutes);
    }
}
