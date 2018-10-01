package danceclass;

import java.util.Map;

public class DanceClassUtil {
    public static final String CLASS_NAME = "name";
    public static final String CLASS_LEVEL = "level";
    public static final String CLASS_INSTRUCTOR = "instructor";
    public static final String CLASS_DATE_RANGE = "dateRange";

    public static DanceClass generateDanceClass(Map<String, String> dataMap) {
        DanceClass danceClass = new DanceClass();

        danceClass.setClassName(dataMap.get(CLASS_NAME));
        danceClass.setLevel(dataMap.get(CLASS_LEVEL));
        danceClass.setInstructorName(dataMap.get(CLASS_INSTRUCTOR));
        danceClass.setWeekNumber(translateDateRangeToWeekNumber(dataMap.get(CLASS_DATE_RANGE)));

        return danceClass;
    }

    // TODO: write this method
    public static Integer translateDateRangeToWeekNumber(String dateRange) {
        return 1;
    }
}
