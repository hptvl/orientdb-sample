import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.simple.JSONObject;

import com.orientechnologies.orient.core.sql.parser.ParseException;

import models.Event;
import orientdb.OrientDBFactory;
import orientdb.OrientDBSelectQuery;
import parsing.JsonParser;

public class QueryDataMain {

	public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
		OrientDBFactory.init("remote:192.168.10.212/test-2", "root", "igloosec", 1, 10, true);
		try {
			long sTime = System.nanoTime();
			// List<Event> result =
			// OrientDBSelectQuery.getEventsByAlertId("esm_20180425135454_15775", 24 * 60 *
			// 60, true);
			List<Event> result = OrientDBSelectQuery.getEvents("[Dcert_웹취약점] 웹쉘(WebShell) 공격 탐지", 24 * 60 * 60, true);
			long eTime = System.nanoTime();
			long elapsedTime = eTime - sTime;
			System.out.println("##########################################");
			System.out.println("Query Elapsed: " + elapsedTime + " nano seconds");
			System.out.println("Query Seconds: " + elapsedTime / 1000000000.0);
			sTime = System.nanoTime();
			JsonParser parser = new JsonParser();
			JSONObject json = parser.createOntologyJson(result);
			System.out.println(json.toJSONString());
			eTime = System.nanoTime();
			elapsedTime = eTime - sTime;
			System.out.println("##########################################");
			System.out.println("Parser Elapsed: " + elapsedTime + " nano seconds");
			System.out.println("Parser Seconds: " + elapsedTime / 1000000000.0);
		} finally {
			OrientDBFactory.close();
		}
	}

}
