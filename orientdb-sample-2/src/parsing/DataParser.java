package parsing;

import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;

import utils.ESMOntGeneralUtil;
import utils.ESMOntNetworkUtil;

public class DataParser {
	private static String[] IPS_FIELDS = { "origin", "mgr_time", "logtype", "s_info", "s_port", "d_info", "d_port",
			"method", "product" };
	private static String[] WAF_FIELDS = { "origin", "mgr_time", "logtype", "s_info", "s_port", "d_info", "d_port",
			"attack" };
	private static String[] ESM_FIELDS = { "seq", "occur_time", "end_time", "rule_name", "log_src_ip", "log_src_name",
			"s_ip", "s_port", "d_ip", "d_port", "method" };

	public static ArrayList<Map<String, Object>> parceCSVAlert(String path) throws Exception {
		ArrayList<Map<String, Object>> ontList = new ArrayList<Map<String, Object>>();

		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(ESM_FIELDS).parse(in);
		SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		String id = "esm_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		for (CSVRecord record : records) {
			if (record.getRecordNumber() == 1) { // header
				continue;
			}
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", id + "_" + record.getRecordNumber());
			obj.put("occurTime", ESMOntGeneralUtil.convertDateString2Unixtime(record.get(ESM_FIELDS[1]), timeFormat));
			obj.put("endTime", ESMOntGeneralUtil.convertDateString2Unixtime(record.get(ESM_FIELDS[2]), timeFormat));
			obj.put("logType", "ESM");

			obj.put("logSourceIP", record.get(ESM_FIELDS[4]));
			obj.put("logSourceName", record.get(ESM_FIELDS[5]));
			obj.put("sourceIP", ESMOntNetworkUtil.getRealIP(record.get(ESM_FIELDS[6])));
			obj.put("sourcePort",
					(!StringUtils.isEmpty(record.get(ESM_FIELDS[7]))
							&& StringUtils.isNumeric(record.get(ESM_FIELDS[7])))
									? Integer.parseInt(record.get(ESM_FIELDS[7]))
									: 0);
			obj.put("destinationIP", ESMOntNetworkUtil.getRealIP(record.get(ESM_FIELDS[8])));
			obj.put("destinationPort",
					(!StringUtils.isEmpty(record.get(ESM_FIELDS[9]))
							&& StringUtils.isNumeric(record.get(ESM_FIELDS[9])))
									? Integer.parseInt(record.get(ESM_FIELDS[9]))
									: 0);
			obj.put("method", record.get(ESM_FIELDS[10]));

			ontList.add(obj);
		}

		in.close();
		return ontList;
	}

	public static ArrayList<Map<String, Object>> parceCSVIPS(String path) throws Exception {
		ArrayList<Map<String, Object>> ontList = new ArrayList<Map<String, Object>>();

		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(IPS_FIELDS).parse(in);

		String id = "ips_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		for (CSVRecord record : records) {
			if (record.getRecordNumber() == 1) { // header
				continue;
			}
			try {
				Map<String, Object> ont = new HashMap<String, Object>();
				ont.put("id", id + "_" + record.getRecordNumber());

				ont.put("origin", record.get("origin"));
				ont.put("managerTime", ESMOntGeneralUtil.convertDateString2Unixtime(
						record.get("mgr_time").substring(0, 14), new SimpleDateFormat("yyyyMMddHHmmss")));

				ont.put("logType", record.get("logtype"));
				ont.put("sourceIP", ESMOntNetworkUtil.getRealIP(record.get("s_info")));
				ont.put("sourcePort",
						(!StringUtils.isEmpty(record.get("s_port")) && StringUtils.isNumeric(record.get("s_port")))
								? Integer.parseInt(record.get("s_port"))
								: 0);
				ont.put("destinationIP", ESMOntNetworkUtil.getRealIP(record.get("d_info")));
				ont.put("destinationPort",
						(!StringUtils.isEmpty(record.get("d_port")) && !StringUtils.isNumeric(record.get("d_port")))
								? Integer.parseInt(record.get("d_port"))
								: 0);
				ont.put("method", record.get("method"));
				ont.put("product", record.get("product"));

				ontList.add(ont);
			} catch (Exception ex) {
				System.out.println(record.getRecordNumber());
			}
		}

		in.close();
		return ontList;
	}

	// origin,mgr_time,logType,s_info,s_port,d_info,d_port,attack
	public static ArrayList<Map<String, Object>> parceCSVWAF(String path) throws Exception {
		ArrayList<Map<String, Object>> ontList = new ArrayList<Map<String, Object>>();

		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(WAF_FIELDS).parse(in);
		String id = "waf_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		for (CSVRecord record : records) {
			if (record.getRecordNumber() == 1) { // header
				continue;
			}
			Map<String, Object> ont = new HashMap<String, Object>();

			ont.put("id", id + "_" + record.getRecordNumber());
			ont.put("origin", record.get("origin"));
			ont.put("managerTime", ESMOntGeneralUtil.convertDateString2Unixtime(record.get("mgr_time").substring(0, 14),
					new SimpleDateFormat("yyyyMMddHHmmss")));
			// ont.put("managerTimeStr", record.get("mgr_time").substring(0, 14));
			ont.put("logType", record.get("logtype"));
			ont.put("sourceIP", ESMOntNetworkUtil.getRealIP(record.get("s_info")));
			ont.put("sourcePort",
					(!StringUtils.isEmpty(record.get("s_port")) && StringUtils.isNumeric(record.get("s_port")))
							? Integer.parseInt(record.get("s_port"))
							: 0);
			ont.put("destinationIP", ESMOntNetworkUtil.getRealIP(record.get("d_info")));
			ont.put("destinationPort",
					(!StringUtils.isEmpty(record.get("d_port")) && StringUtils.isNumeric(record.get("d_port")))
							? Integer.parseInt(record.get("d_port"))
							: 0);
			ont.put("method", record.get("attack"));

			ontList.add(ont);
		}

		in.close();
		return ontList;
	}
}
