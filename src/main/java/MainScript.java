import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import danceDay.DanceDay;
import danceclass.DanceClass;
import danceclass.DanceClassUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
            String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            List<DanceDay> weeklySchedule = Lists.newArrayList();
            for (int day = 0; day < 7; day++) {
                DanceDay danceDay = new DanceDay(daysOfWeek[day]);

                HtmlTable classScheduleTable = classSchedulePage.getHtmlElementById("scrollingtable");
                List<HtmlTableBody> bodies = classScheduleTable.getBodies();
                HtmlTableBody tableBody = bodies.get(0);
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
                    List<DanceClass> danceClasses = getDanceClassesFromRow(row, requestedClassName, requestedClassLevel);
                    danceDay.addClasses(danceClasses);
                }

                weeklySchedule.add(danceDay);

                // TODO: STEP 2: GET INFO FROM REST OF THE DAYS
                // once done, click anchor tag to get next day
                HtmlDivision dateBoxesContainer = classSchedulePage.getHtmlElementById("dateUL");
                Iterable<DomNode> dateBoxes = dateBoxesContainer.getChildren();
                Iterator<DomNode> dateBoxIterator = dateBoxes.iterator();


                // 3rd node is current day and 5th node is next day
                DomNode box1 = dateBoxIterator.next(); // useless input tag
                DomNode box2 = dateBoxIterator.next(); // current day
                DomNode box3 = dateBoxIterator.next(); // next day
                DomNode box4 = dateBoxIterator.next();
                DomNode box5 = dateBoxIterator.next();

                if (box5 instanceof HtmlAnchor) {
                    classSchedulePage = ((HtmlAnchor) box5).click();
                }
            }


            // TODO: write collection print method
            printMethod(weeklySchedule, requestedClassName, requestedClassLevel);
        }
    }

    public static List<DanceClass> getDanceClassesFromRow(HtmlTableRow row, String requestedClassName, String requestedClassLevel) {
        List<DanceClass> danceClasses = Lists.newArrayList();

        for (HtmlTableCell rowCell : row.getCells()) {
            Iterator<DomNode> cellData = rowCell.getChildren().iterator();

            List<String> dataPoints = extractClassDataFromCellData(cellData);
            if (dataPoints.size() < 4) {
                continue;
            }

            Map<String, String> classDataMap = convertListToClassMap(dataPoints);

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

    public static Map<String, String> convertListToClassMap(List<String> dataList) {
        Map<String, String> classDataMap = Maps.newHashMap();

        String name = dataList.get(0);
        classDataMap.put(DanceClassUtil.CLASS_NAME, name);

        String level = dataList.get(1);
        classDataMap.put(DanceClassUtil.CLASS_LEVEL, level);

        String instructor = dataList.get(2);
        classDataMap.put(DanceClassUtil.CLASS_INSTRUCTOR, instructor);

        String dateRange = dataList.get(3);
        classDataMap.put(DanceClassUtil.CLASS_DATE_RANGE, dateRange);

        return classDataMap;
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
        System.out.println("Requested Level: " + requestedLevel);

        for (DanceDay danceDay : days) {
            System.out.println("--------------------");
            System.out.println(danceDay);
        }

        System.out.println("\nNow go ahead and dance! :)");
    }
}
