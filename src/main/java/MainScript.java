import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;


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
            final HtmlPage classSchedulePage = webclient.getPage("http://www.pielcaneladancers.com/classschedule");

            // STEP 1: GET INFO FROM FIRST DAY
            final HtmlTable classScheduleTable = classSchedulePage.getHtmlElementById("scrollingtable");
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

                // since is class row, iterate through classes in Class Row
                for (HtmlTableCell danceClass : row.getCells()) {
                    // iterate through data points (span tags) in class div
                    Iterator<DomNode> dataIterator = danceClass.getChildren().iterator();
                    List<String> dataPoints = Lists.newArrayList(); // 0: className, 1: level, 2: instructor 3: date

                    Integer index = 0;
                    while (dataIterator.hasNext()) {
                        DomNode currentNode = dataIterator.next();
                        String textContent = currentNode.getTextContent();
                        if (textContent.isEmpty()) {
                            continue;
                        }
                        dataPoints.add(index++, textContent);
                    }

                    if (index < 3) {
                        System.out.println("Class div " + danceClass + " is malformed...ignoring");
                        continue;
                    }

                    // TODO: add method that converts className to requested format or vice versa
                    // TODO: add method that converts classLevel "" ""
                    // TODO: write class print method
                    if (!dataPoints.get(0).trim().equals(requestedClassName) || !dataPoints.get(1).trim().equals(requestedClassLevel)) {
                        System.out.println("Not requested class");
                        continue;
                    } else {
                        System.out.println("Found the desired class!!!");
                    }
                }
            }


            // TODO: STEP 2: GET INFO FROM REST OF THE DAYS
            // TODO: write collection print method
        }
    }
}
