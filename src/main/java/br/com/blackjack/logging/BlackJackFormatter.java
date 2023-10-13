package br.com.blackjack.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class BlackJackFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getLevel() +
                " " +
                record.getSourceClassName() +
                "[" +
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                "]" +
                ": " +
                record.getMessage() +
                "\n";
    }
}
