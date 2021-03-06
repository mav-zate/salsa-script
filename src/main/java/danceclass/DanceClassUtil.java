package danceclass;

import java.time.LocalDate;
import java.util.Map;

import static java.time.temporal.ChronoUnit.WEEKS;

public class DanceClassUtil {
    public static final String CLASS_NAME = "name";
    public static final String CLASS_LEVEL = "level";
    public static final String CLASS_INSTRUCTOR = "instructor";
    public static final String CLASS_START_TIME = "startTime";
    public static final String CLASS_START_DATE = "startDate";
  public static final String CURRENT_DATE = "currentDate";
    public static final String CLASS_END_TIME = "endTime";

    public static DanceClass generateDanceClass(Map<String, Object> dataMap) {
        DanceClass danceClass = new DanceClass();

        danceClass.setClassName((String) dataMap.get(CLASS_NAME));
        danceClass.setLevel((String) dataMap.get(CLASS_LEVEL));
        danceClass.setInstructorName((String) dataMap.get(CLASS_INSTRUCTOR));
        danceClass.setWeekNumber(translateDateRangeToWeekNumber(dataMap));
        danceClass.setStartTime((ClassTime) dataMap.get(CLASS_START_TIME));
        danceClass.setEndTime((ClassTime) dataMap.get(CLASS_END_TIME));

        return danceClass;
    }

    public static Integer translateDateRangeToWeekNumber(Map<String, Object> dataMap) {
      String startDate = (String) dataMap.get(CLASS_START_DATE);
      LocalDate start = convertDateStringToDate(startDate.trim());

      String currentDate = (String) dataMap.get(CURRENT_DATE);
      LocalDate current = convertDateStringToDate(currentDate.trim());
      return getWeekNumber(start, current);
    }

    private static LocalDate convertDateStringToDate(String dateString) {
      String[] monthAndDay = dateString.split(" ");
      String month = monthAndDay[0].toUpperCase();
      String day = monthAndDay[1];
      MonthNumber monthNumber = MonthNumber.valueOf(month);
      return LocalDate.of(LocalDate.now().getYear(),
          monthNumber.getMonthNumber(),
          Integer.parseInt(day));
    }

    private static int getWeekNumber(LocalDate startDate, LocalDate currentDate) {
      return (int) WEEKS.between(startDate, currentDate) + 1;
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
