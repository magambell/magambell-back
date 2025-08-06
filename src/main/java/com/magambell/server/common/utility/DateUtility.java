package com.magambell.server.common.utility;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtility {

    private static final Integer PRESIGNED_EXPIRE_MINUTES = 5;

    public static Date getPresignedExpireDate() {
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime oneMinuteLater = nowUTC.plusMinutes(PRESIGNED_EXPIRE_MINUTES);
        return Date.from(oneMinuteLater.toInstant());

    }
}
