package orientdb;

import java.util.Date;

public class OrientDBDeleteQuery {
	public static void deleteVertex(String id) {
		String query = "DELETE VERTEX Event where id=?";
		OrientDBFactory.executeQuery(query, new Object[] { id });
	}

	public static void deleteVertices(int numDays) {
		long maxTime = (long) (new Date()).getTime() / 1000;
		long minTime = maxTime - numDays * 24 * 60 * 60;

		deleteAlerts(minTime, maxTime);
		deleteIPSs(minTime, maxTime);
		deleteWAFs(minTime, maxTime);
	}

	public static void deleteAllVertices() {
		deleteAllAlerts();
		deleteAllIPSs();
		deleteAllWAFs();
	}

	public static void deleteAlerts(long minTime, long maxTime) {
		String query = "DELETE VERTEX Alert WHERE occurTime BETWEEN ? AND ?";
		OrientDBFactory.executeQuery(query, new Object[] { minTime, maxTime });
	}

	public static void deleteAllAlerts() {
		String query = "DELETE VERTEX Alert";
		OrientDBFactory.executeQuery(query, null);
	}

	public static void deleteIPSs(long minTime, long maxTime) {
		String query = "DELETE VERTEX IPS WHERE managerTime BETWEEN ? AND ?";
		OrientDBFactory.executeQuery(query, new Object[] { minTime, maxTime });
	}

	public static void deleteAllIPSs() {
		String query = "DELETE VERTEX IPS";
		OrientDBFactory.executeQuery(query, null);
	}

	public static void deleteWAFs(long minTime, long maxTime) {
		String query = "DELETE VERTEX WAF WHERE managerTime BETWEEN ? AND ?";
		OrientDBFactory.executeQuery(query, new Object[] { minTime, maxTime });
	}

	public static void deleteAllWAFs() {
		String query = "DELETE VERTEX WAF";
		OrientDBFactory.executeQuery(query, null);
	}

}
