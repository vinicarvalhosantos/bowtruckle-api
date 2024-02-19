package br.com.santos.vinicius.bowtruckleapi.util;

import br.com.santos.vinicius.bowtruckleapi.model.dto.GenericTimeDTO;

import java.time.LocalTime;

public class DateTimeUtils {

    public static boolean isDayBreak() {
        LocalTime now = LocalTime.now();
        LocalTime midnight = LocalTime.of(0, 0, 0);
        LocalTime dawn = LocalTime.of(5, 0, 0);

        return now.isAfter(midnight) && now.isBefore(dawn);
    }

    public static GenericTimeDTO formatTimeFromSeconds(long totalSeconds) {
        LocalTime gameLocalTime = LocalTime.ofSecondOfDay(totalSeconds);
        String[] epochTimes = gameLocalTime.toString().split(":");
        GenericTimeDTO timeDTO = new GenericTimeDTO();
        timeDTO.setHours(Long.parseLong(epochTimes[0]));
        timeDTO.setMinutes(Long.parseLong(epochTimes[1]));
        timeDTO.setSeconds(epochTimes.length == 3 ? Long.parseLong(epochTimes[2]) : 0);
        String timeFormatted = formatFriendlyTime(epochTimes);
        timeDTO.setFormattedMessage(timeFormatted);

        return timeDTO;
    }

    public static String formatFriendlyTime(String[] array) {
        StringBuilder timeFormatted = new StringBuilder();
        int hours = Integer.parseInt(array[0]);
        int minutes = Integer.parseInt(array[1]);
        int seconds = array.length == 3 ? Integer.parseInt(array[2]) : 0;

        if (hours > 0) {
            timeFormatted.append(String.format("%d hour%s, ", hours, hours > 1 ? "s" : ""));
        }

        if (minutes > 0 || hours > 0) {
            timeFormatted.append(String.format("%d minute%s and ", minutes, minutes > 1 ? "s" : ""));
        }

        timeFormatted.append(String.format("%d second%s.", seconds, seconds > 1 ? "s" : ""));

        return timeFormatted.toString();
    }
}
