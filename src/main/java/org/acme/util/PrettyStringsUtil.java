package org.acme.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class PrettyStringsUtil {
    public static String formatMoney(BigDecimal value){
        return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }
    public static String formatDate(LocalDateTime date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(Locale.US);
        return date.format(dateTimeFormatter);
    }

}
