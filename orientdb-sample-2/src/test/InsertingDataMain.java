import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import orientdb.OrientDBDeleteQuery;
import orientdb.OrientDBFactory;
import orientdb.OrientDBInsertQuery;
import orientdb.OrientDBSelectQuery;
import parsing.DataParser;

public class InsertingDataMain {

	private static LinkedHashMap<String, Object> _1_DAY_ALERT_CACHE = new LinkedHashMap<String, Object>();

	public static List<Map<String, Object>> parseData() throws Exception {
		String base = "./testfile";
		long sTime = System.nanoTime();
		ArrayList<Map<String, Object>> objList = new ArrayList<Map<String, Object>>();
		objList.addAll(DataParser.parceCSVAlert(base + "/esm.csv"));
		objList.addAll(DataParser.parceCSVIPS(base + "/ips.csv"));
		objList.addAll(DataParser.parceCSVWAF(base + "/waf.csv"));
		long eTime = System.nanoTime();
		long elapsedTime = eTime - sTime;
		System.out.println("Number of obj:" + objList.size());
		System.out.println("Parsed Elapsed: " + elapsedTime + " nano seconds");
		System.out.println("Parsed Seconds: " + TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
		sTime = System.nanoTime();
		return objList;
	}

	public static void insertData(List<Map<String, Object>> objList) {
		// reset cache
		String cacheDate = _1_DAY_ALERT_CACHE.get("DATE").toString();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		if (!format.format(new Date()).equals(cacheDate)) {
			_1_DAY_ALERT_CACHE.clear();
			_1_DAY_ALERT_CACHE.put("DATE", format.format(new Date()));
		}
		// set allow graph using id as a custom property
		OrientDBFactory.setStandardElementConstraints(false);
		// use lightweight edge mode
		OrientDBFactory.setUseLightweightEdges(true);
		int start = 0;
		int MAX = objList.size();
		int batchSize = OrientDBFactory.MAX_INSERT_TRANSACTION;
		int end = 0;
		long sTime = System.nanoTime();
		OrientDBFactory.setMassiveInsert(true);
		do {
			if (start + batchSize < MAX) {
				end = start + batchSize;
			} else {
				end = MAX - 1;
			}
			long srcIp = 0L;
			long dstIp = 0L;
			String logType = "";
			for (int i = start; i <= end; i++) {
				srcIp = (long) objList.get(i).get("sourceIP");
				dstIp = (long) objList.get(i).get("destinationIP");
				OrientVertex event = null;
				logType = (String) objList.get(i).get("logType");
				if ("ESM".equals(logType)) {
					event = OrientDBInsertQuery.insertVertex("Alert", objList.get(i));
					_1_DAY_ALERT_CACHE.put(srcIp + "_" + dstIp, event);
				} else if ("IPS".equals(logType)) {
					event = OrientDBInsertQuery.insertVertex("IPS", objList.get(i));
				} else if ("WAF".equals(logType)) {
					event = OrientDBInsertQuery.insertVertex("WAF", objList.get(i));
				}
				// only add an edge when log is WAF or IPS
				if ("IPS".equals(logType) || "WAF".equals(logType)) {
					OrientVertex alert = (OrientVertex) _1_DAY_ALERT_CACHE.get(srcIp + "_" + dstIp);
					if (alert != null) {
						OrientDBInsertQuery.insertEdge("connect", alert, event);
					}
				}
			}
			OrientDBFactory.commit();
			start = end + 1;
		} while (end < MAX - 1);
		OrientDBFactory.setMassiveInsert(false);
		long eTime = System.nanoTime();
		long elapsedTime = eTime - sTime;
		System.out.println("##########################################");
		System.out.println("Added Elapsed: " + elapsedTime + " nano seconds");
		System.out.println("Added Seconds: " + TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
	}

	public static void main(String[] args) throws Exception {
		List<Map<String, Object>> objList = parseData();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		_1_DAY_ALERT_CACHE.put("DATE", format.format(new Date()));
		OrientDBFactory.init("remote:192.168.10.212/test-2", "root", "igloosec", 1, 10, true);
		try {
			insertData(objList);
		} finally {
			OrientDBFactory.close();
		}

	}

}
