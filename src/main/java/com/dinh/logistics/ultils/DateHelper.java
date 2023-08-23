package com.dinh.logistics.ultils;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Slf4j
public class DateHelper {
	public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
	
	public static Date convertDate(String format, String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
		return date;
	}

    public static Date convertDateDefault(String format, String value) {
        Date date = null;
        try {
            Date fromDates = DateHelper.convertDate(format, value);
            LocalDate localDate = fromDates.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String dateFormatNotify(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/M/yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String convertDateToStringMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String convertDateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(date);
        return strDate;
    }


    public static String convertDateToStringJobList(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }
    
    public static String convertDateTimeToStringSubmitCore(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String convertDateToStringSubmitCoreSql(java.sql.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static String convertDateTimeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static Timestamp getCurrentTimestamp() {
        Instant instant = Instant.now();
        Timestamp timestamp = instant != null ? new Timestamp(instant.toEpochMilli()) : new Timestamp(LocalTime.now().getNano());
        return timestamp;
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static String getDatePastByDay(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -day);
        return convertDateToString(cal.getTime());
    }

    public static Date formatStringToDate(String date) {
	    try {
	        if (StringUtils.isNotBlank(date)) {
                return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
            }
        }catch (Exception e) {
        	log.error("formatStringToDate: "+ e.getMessage());
        }
        return null;
    }
    
    public static Date formatStringToDateV2(String date) {
	    try {
	        if (StringUtils.isNotBlank(date)) {
                return new SimpleDateFormat("dd/MM/yyyy").parse(date);
            }
        }catch (Exception e) {
        	log.error("formatStringToDate: "+ e.getMessage());
        }
        return null;
    }

    public static Date formatStringToDateSyncAccount(String date) {

        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }catch (Exception e) {
        	log.error("formatStringToDateSyncAccount: "+ e.getMessage());
        }
        return null;
    }

    public static String convertDateToTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String strDate = formatter.format(date);
        return strDate;
    }
    
    public static String convertDateStringByFormat(Date date, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			String strDate = formatter.format(date);
			return strDate;
		} catch (Exception e) {
		}
		return null;
	}

    public static String convertDateTimeCheckNull(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDate = formatter.format(date);
            return strDate;
        } else {
            return null;
        }
    }
}
