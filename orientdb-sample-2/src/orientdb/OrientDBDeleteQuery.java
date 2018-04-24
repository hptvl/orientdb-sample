package orientdb;

import java.util.Date;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;

public class OrientDBDeleteQuery {
	public static void deleteVertex(String id) {
		String query = "DELETE VERTEX Event where id=\"" + id + "\"";
		OrientDBFactory.executeQuery(query);
	}

	public static void deleteVertices(OrientBaseGraph graph, int numDays) {
		long maxTime = (long) (new Date()).getTime() / 1000;
		long minTime = maxTime - numDays * 24 * 60 * 60;

		deleteAlerts(graph, minTime, maxTime);
		deleteIPSs(graph, minTime, maxTime);
		deleteWAFs(graph, minTime, maxTime);
	}

	public static void deleteAlerts(OrientBaseGraph graph, long minTime, long maxTime) {
		String query = "DELETE VERTEX Alert WHERE occurTime BETWEEN #minTime# AND #maxTime#";
		query = query.replace("#maxTime#", String.valueOf(maxTime));
		query = query.replace("#minTime#", String.valueOf(minTime));
		OrientDBFactory.executeQuery(query);
	}

	public static void deleteIPSs(OrientBaseGraph graph, long minTime, long maxTime) {
		String query = "DELETE VERTEX IPS WHERE managerTime BETWEEN #minTime# AND #maxTime#";
		query = query.replace("#maxTime#", String.valueOf(maxTime));
		query = query.replace("#minTime#", String.valueOf(minTime));
		OrientDBFactory.executeQuery(query);
	}

	public static void deleteWAFs(OrientBaseGraph graph, long minTime, long maxTime) {
		String query = "DELETE VERTEX WAF WHERE managerTime BETWEEN #minTime# AND #maxTime#";
		query = query.replace("#maxTime#", String.valueOf(maxTime));
		query = query.replace("#minTime#", String.valueOf(minTime));
		OrientDBFactory.executeQuery(query);
	}
}
