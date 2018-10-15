import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import danceDay.DanceDay;
import danceclass.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainScript {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Incorrect number of arguments given. First argument must be class name " +
                    "(all lower case, spaces replaced by underscores) " +
                    "Second argument must be class level written as a single digit.\n" +
                    "Example: SalsaOnTwo 2");
            return;
        }
        final String requestedClassName = args[0];
        final String requestedClassLevel = args[1];

        try (final WebClient webclient = new WebClient()) {
            HtmlPage classSchedulePage = webclient.getPage("http://www.pielcaneladancers.com/classschedule");

            List<DanceDay> weeklySchedule = Lists.newArrayList();
            for (int day = 0; day < 7; day++) {
                HtmlDivision dateBoxesContainer = classSchedulePage.getHtmlElementById("dateUL");
                Iterable<DomNode> dateBoxes = dateBoxesContainer.getChildren();
                Iterator<DomNode> dateBoxIterator = dateBoxes.iterator();


                // 3rd node is current day and 5th node is next day
                DomNode box1 = dateBoxIterator.next(); // useless input tag
                DomNode box2 = dateBoxIterator.next();
                DomNode box3 = dateBoxIterator.next(); // current day
                DomNode box4 = dateBoxIterator.next();
                DomNode box5 = dateBoxIterator.next();

                String dayString = box3.getTextContent().trim().replace("\n", " ").trim();
                String[] dayParts = dayString.split(" ");
                String dayName = dayParts[0];
                String month = dayParts[1];
                String date = dayParts[2];


                DanceDay danceDay = new DanceDay(dayName);


                HtmlTable classScheduleTable = classSchedulePage.getHtmlElementById("scrollingtable");
                List<HtmlTableBody> bodies = classScheduleTable.getBodies();
                HtmlTableBody tableBody = bodies.get(0);
                List<ClassTime> classStartTimes = getClassStartTimes(tableBody);
                for (HtmlTableRow row : tableBody.getRows()) {
                    // check if row is class row or not
                    HtmlTableCell firstTableCell = row.getCell(0);
                    String colSpanVal = firstTableCell.getAttribute("colspan");
                    if (colSpanVal.isEmpty()) {
                        continue;
                    }
                    Integer colSpanInt = Integer.parseInt(colSpanVal);

                    if (colSpanInt < 1 || colSpanInt > 5) {
                        continue;
                    }
                    List<DanceClass> danceClasses = getDanceClassesFromRow(row, requestedClassName, requestedClassLevel, classStartTimes, month + " " + date);
                    danceDay.addClasses(danceClasses);
                }

                weeklySchedule.add(danceDay);

                if (box5 instanceof HtmlAnchor) {
                    classSchedulePage = ((HtmlAnchor) box5).click();
                }
            }


            // TODO: write collection print method
            printMethod(weeklySchedule, requestedClassName, requestedClassLevel);
        }
    }

    public static List<DanceClass> getDanceClassesFromRow(HtmlTableRow row, String requestedClassName,
                                                          String requestedClassLevel, List<ClassTime> classStartTimes,
                                                          String currentDay) {
        List<DanceClass> danceClasses = Lists.newArrayList();
        Integer totalColSpan = 0;

        for (HtmlTableCell rowCell : row.getCells()) {
            String colSpan = rowCell.getAttribute("colspan");
            Integer colSpanVal;
            if (colSpan.isEmpty()) {
                System.out.println("Cell is corrupt...colSpan attribute is non-existent or null");
                return null;
            }
            colSpanVal = Integer.parseInt(colSpan);

            ClassTime startTime = classStartTimes.get(totalColSpan);
            ClassTime endTime;
            try {
                endTime = DanceClassUtil.getClassEndTime(startTime, colSpanVal);
            } catch (AbnormalClassLength e) {
                continue; // TODO: ensure continue skips for iteration, out of catch block
            }
            totalColSpan += colSpanVal;

            Iterator<DomNode> cellData = rowCell.getChildren().iterator();

            List<String> dataPoints = extractClassDataFromCellData(cellData);
            if (dataPoints.size() < 4) {
                continue;
            }

            Map<String, Object> classDataMap = convertListToClassMap(dataPoints);
            classDataMap.put(DanceClassUtil.CLASS_START_TIME, startTime);
            classDataMap.put(DanceClassUtil.CLASS_END_TIME, endTime);
            classDataMap.put(DanceClassUtil.CURRENT_DATE, currentDay);

            // TODO: add method that converts className to requested format or vice versa
            // TODO: add method that converts classLevel "" ""
            // TODO: write class print method
            if (!dataPoints.get(0).trim().equals(requestedClassName) || !dataPoints.get(1).trim().equals(requestedClassLevel)) {
                continue;
            } else {
                DanceClass danceClass = DanceClassUtil.generateDanceClass(classDataMap);
                danceClasses.add(danceClass);
            }
        }

        return danceClasses;
    }

    public static Map<String, Object> convertListToClassMap(List<String> dataList) {
        Map<String, Object> classDataMap = Maps.newHashMap();

        String name = dataList.get(0);
        classDataMap.put(DanceClassUtil.CLASS_NAME, name);

        String level = dataList.get(1);
        classDataMap.put(DanceClassUtil.CLASS_LEVEL, level);

        String instructor = dataList.get(2);
        classDataMap.put(DanceClassUtil.CLASS_INSTRUCTOR, instructor);

        String dateRange = dataList.get(3);
        classDataMap.put(DanceClassUtil.CLASS_START_DATE, extractStartDate(dateRange));

        return classDataMap;
    }

    public static String extractStartDate(String dateRange) {
        String[] parts = dateRange.split(" - ");
        return parts[0].trim();
    }

    public static List<String> extractClassDataFromCellData(Iterator<DomNode> cellData) {
        List<String> dataPoints = Lists.newArrayList();

        Integer index = 0;
        while (cellData.hasNext()) {
            DomNode currentNode = cellData.next();
            String textContent = currentNode.getTextContent();
            if (textContent.isEmpty()) {
                continue;
            }
            dataPoints.add(index++, textContent);
        }

        return dataPoints;
    }

    public static void printMethod(List<DanceDay> days, String requestedClass, String requestedLevel) {
        System.out.println("Requested Class: " + requestedClass);
        System.out.println("Requested Level: " + requestedLevel + "\n");

        for (DanceDay danceDay : days) {
            System.out.println("--------------------");
            System.out.println(danceDay);
        }

        System.out.println("\nNow go ahead and dance! :)");
    }

    public static List<ClassTime> getClassStartTimes(HtmlTableBody tableBody) {
        List<ClassTime> classStartTimes = Lists.newArrayList();

        for (HtmlTableRow row : tableBody.getRows()) {
            List<HtmlTableCell> tableCells = row.getCells();
            Boolean isTimeRow = tableCells
                    .stream()
                    .anyMatch((cell) -> {
                        String className = cell.getAttribute("class");
                        return (className.equals("time_td_tri") || className.equals("time_td"));
                    });

            if (isTimeRow) {
                classStartTimes.addAll(extractClassTimesFromRow(row));
            }
        }

        return classStartTimes;
    }

    public static List<ClassTime> extractClassTimesFromRow(HtmlTableRow row) {
        List<ClassTime> classTimes = Lists.newArrayList();

        for (HtmlTableCell cell : row.getCells()) {
            classTimes.add(classTimeFromCell(cell));
        }

        // add school closing time
        ClassTime schoolCloseTime = classTimes.get(classTimes.size() - 1);

        return classTimes;
    }

    public static ClassTime classTimeFromCell(HtmlTableCell cell) {
        String classStartTime = cell.getTextContent();

        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(classStartTime);
        if (m.find()) {
            int idx = m.start();
            classStartTime = classStartTime.substring(0, idx);
        }
        String[] timeUnits = classStartTime.split(":");
        ClassTime classTime = new ClassTime(Integer.parseInt(timeUnits[0]), Integer.parseInt(timeUnits[1]));

        return classTime;
    }
}
