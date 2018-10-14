package danceclass;

import java.util.Map;

public class DanceClassUtil {
    public static final String CLASS_NAME = "name";
    public static final String CLASS_LEVEL = "level";
    public static final String CLASS_INSTRUCTOR = "instructor";
    public static final String CLASS_DATE_RANGE = "dateRange";
    public static final String CLASS_START_TIME = "startTime";
    public static final String CLASS_END_TIME = "endTime";

    public static DanceClass generateDanceClass(Map<String, Object> dataMap) {
        DanceClass danceClass = new DanceClass();

        danceClass.setClassName((String) dataMap.get(CLASS_NAME));
        danceClass.setLevel((String) dataMap.get(CLASS_LEVEL));
        danceClass.setInstructorName((String) dataMap.get(CLASS_INSTRUCTOR));
        danceClass.setWeekNumber(translateDateRangeToWeekNumber((String) dataMap.get(CLASS_DATE_RANGE)));
        danceClass.setStartTime((ClassTime) dataMap.get(CLASS_START_TIME));
        danceClass.setEndTime((ClassTime) dataMap.get(CLASS_END_TIME));

        return danceClass;
    }

    // TODO: write this method
    public static Integer translateDateRangeToWeekNumber(String dateRange) {
        return 1;
    }

    public static ClassTime getClassEndTime(ClassTime classStartTime, Integer colSpanValue) throws
        AbnormalClassLength {
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
