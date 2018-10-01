package danceDay;

import com.google.common.collect.Lists;
import danceclass.DanceClass;

import java.util.List;

public class DanceDay {
    private final List<DanceClass> danceClasses = Lists.newArrayList();
    private String day;

    public DanceDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(day).append(":\n");
        if (danceClasses.size() > 0) {
            for (DanceClass danceClass : danceClasses) {
                sb.append(danceClass);
            }
        } else {
            sb.append("NO CLASSES");
        }

        return sb.toString();
    }

    public void addClasses(List<DanceClass> danceClasses) {
        if (danceClasses.isEmpty()) {
            return;
        }
        this.danceClasses.addAll(danceClasses);
    }

    public void addClass(DanceClass danceClass) {
        danceClasses.add(danceClass);
    }
}
