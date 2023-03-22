package com.avro.le;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ScheduleComparator implements Comparator<Schedule> {
    @Override
    public int compare(Schedule s1, Schedule s2) {
        // Default comparison method - compares by startDateTime
        return compareByStartDateTime(s1, s2);
    }

    public int compareByStartDateTime(Schedule s1, Schedule s2) {
        LocalDateTime s1StartDateTime = LocalDateTime.parse(s1.getStartDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (s2 == null) {
            return -1;
        }
        LocalDateTime s2StartDateTime = LocalDateTime.parse(s2.getStartDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return s1StartDateTime.compareTo(s2StartDateTime);
    }

    public int compareByEndDateTime(Schedule s1, Schedule s2) {
        LocalDateTime s1EndDateTime = LocalDateTime.parse(s1.getEndDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (s2 == null) {
            // If s2 is null, return a value greater than any other value
            return 1;
        }
        LocalDateTime s2EndDateTime = LocalDateTime.parse(s2.getEndDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return s1EndDateTime.compareTo(s2EndDateTime);
    }

}


