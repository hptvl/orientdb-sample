import java.util.List;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import orientdb.OrientDBFactory;
import orientdb.OrientDBSelectQuery;

public class QueryDataMain {

	public static void main(String[] args) {
		OrientDBFactory.init("remote:192.168.10.212/test-2", "root", "igloosec", 1, 10, true);
		try {
			long sTime = System.nanoTime();
			List<OrientVertex> result = OrientDBSelectQuery.getEventsByAlertId("esm_20180424131248_18956", 60 * 60,
					true);
			for (OrientVertex obj : result) {
				// System.out.println(obj.getProperty("method").toString());
				StringBuffer buff = new StringBuffer();
				buff.append("{");
				for (String pro : obj.getPropertyKeys()) {
					buff.append(pro + ":" + obj.getProperty(pro).toString() + ", ");
				}
				buff.append("}");
				System.out.println(buff.toString());
			}

			long eTime = System.nanoTime();
			long elapsedTime = eTime - sTime;
			System.out.println("##########################################");
			System.out.println("Query Elapsed: " + elapsedTime + " nano seconds");
			System.out.println("Query Seconds: " + elapsedTime / 1000000000.0);
		} finally {
			OrientDBFactory.close();
		}
	}

}
