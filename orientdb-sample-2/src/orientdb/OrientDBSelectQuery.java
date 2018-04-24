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
		Object[] params = null;
		if (!includeAlert) {
			query = "SELECT EXPAND(in) FROM `connect` WHERE out.id=?";
			query += " AND in.managerTime + ? <= out.occurTime";
			params = new Object[] { alertId, timeRange };
		} else {
			query = "SELECT EXPAND($all) LET $in = (SELECT EXPAND(in) FROM `connect` WHERE out.id=? AND in.managerTime + ? <= out.occurTime), "
					+ "$out=(SELECT FROM `alert` where id = ?), $all=UNIONALL($in,$out)";
			params = new Object[] { alertId, timeRange, alertId };
		}

		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query, params);
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
		String query = "SELECT FROM Event WHERE id=?";
		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query, new Object[] { id });
		if (result != null) {
			if (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				return obj;
			}
		}
		return null;
	}
}
