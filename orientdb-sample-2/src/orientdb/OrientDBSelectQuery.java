package orientdb;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * Select query instance
 * 
 * @author Tran Ho
 *
 */
public class OrientDBSelectQuery {
	@SuppressWarnings("unchecked")
	public static List<OrientVertex> getEventsByAlertId(String alertId, long timeRange, boolean includeAlert) {
		List<OrientVertex> list = new ArrayList<OrientVertex>();
		String query = "";
		if (!includeAlert) {
			query = "SELECT EXPAND(in) FROM `connect` WHERE out.id=\"" + alertId + "\"";
			query += " AND in.managerTime + " + timeRange + " <= out.occurTime";
		} else {
			query = "SELECT EXPAND($all) LET $in = (SELECT EXPAND(in) FROM `connect` WHERE out.id=\"" + alertId
					+ "\" AND in.managerTime + " + timeRange + " <= out.occurTime), "
					+ "$out=(SELECT FROM `alert` where id = \"" + alertId + "\"), $all=UNIONALL($in,$out)";
		}

		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query);
		if (result != null) {
			while (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				list.add(obj);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static OrientVertex getEvent(String id) {
		String query = "SELECT FROM Event WHERE id=\"" + id + "\"";
		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query);
		if (result != null) {
			if (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				return obj;
			}
		}
		return null;
	}
}
