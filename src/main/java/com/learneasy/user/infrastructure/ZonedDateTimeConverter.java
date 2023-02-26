package com.learneasy.user.infrastructure;

import org.springframework.core.convert.converter.Converter;
import java.time.ZonedDateTime;
import java.util.Date;

public class ZonedDateTimeConverter implements Converter<ZonedDateTime, Date> {

    @Override
    public Date convert(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }
}
