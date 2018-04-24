package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ESMOntGeneralUtil {
	public static final Long convertDateString2Unixtime(String date, DateFormat format) throws ParseException {
		Date d = format.parse(date);
		Long unixTime = (long) d.getTime() / 1000;

		return unixTime;
	}

	public static final String convertDateString2UnixtimeString(String date, DateFormat format) throws ParseException {

		return String.valueOf(ESMOntGeneralUtil.convertDateString2Unixtime(date, format));
	}

	public static void main(String[] args) throws Exception {
		String dt1 = "2017/04/04 17:00:01";
		String dt2 = "2017/04/04 18:00:01";
		String dt3 = "20171104031030";
		DateFormat format = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
		DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
													
		long l1 = convertDateString2Unixtime(dt1, format);
		long l2 = convertDateString2Unixtime(dt2, format);
		long l3 = convertDateString2Unixtime(dt3, format1);
//		System.out.println(l1);
//		System.out.println(l2);
		System.out.println(l3);
//		System.out.println(l2 - l1);
	}
}
