package checklist.util;

import checklist.model.Course;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TermGrouper.java
 *
 * Utility class that groups a list of Course objects by their
 * (year, term) pair, preserving the curriculum order they appear in.
 * Used by multiple feature classes to avoid duplicating grouping logic.
 *
 * Key format: "YEAR|TERM"  (e.g. "1|1st Semester")
 *
 * Author  : Member 3
 * Section : CS 122 - [Section]
 * Date    : [Date]
 */
public class TermGrouper {

    // Prevent instantiation
    private TermGrouper() {}

    /**
     * Groups the given courses into an ordered map keyed by "YEAR|TERM".
     * Insertion order matches the order courses appear in the source list.
     *
     * @param courses the full list of courses to group
     * @return a LinkedHashMap preserving curriculum term order
     */
    public static LinkedHashMap<String, List<Course>> group(List<Course> courses) {
        LinkedHashMap<String, List<Course>> map = new LinkedHashMap<>();
        for (Course c : courses) {
            String key = c.getYear() + "|" + c.getTerm();
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(c);
        }
        return map;
    }
}
