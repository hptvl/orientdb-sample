package orientdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import models.Event;
import utils.Converter;

/**
 * Select query instance
 * 
 * @author Tran Ho
 *
 */
public class OrientDBSelectQuery {
	@SuppressWarnings("unchecked")
	public static List<Event> getEventsByAlertId(String alertId, long timeRange, boolean includeAlert) {
		List<Event> list = new ArrayList<Event>();
		String query = "";
		Object[] params = null;
		if (!includeAlert) {
			query = "SELECT EXPAND(in) FROM `connect` WHERE out.id=?";
			query += " AND out.occurTime - ? <= in.managerTime";
			params = new Object[] { alertId, timeRange };
		} else {
			query = "SELECT EXPAND($all) LET $in = (SELECT EXPAND(in) FROM `connect` WHERE out.id=? AND out.occurTime - ? <= in.managerTime), "
					+ "$out=(SELECT FROM Alert where id = ?), $all=UNIONALL($in,$out)";
			params = new Object[] { alertId, timeRange, alertId };
		}

		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query, params);
		if (result != null) {
			while (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				list.add(Converter.toEvent(obj));
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<Event> getEvents(String ruleName, long timeRange, boolean includeAlert) {
		List<Event> list = new ArrayList<Event>();
		String query = "";
		Object[] params = null;
		if (!includeAlert) {
			query = "SELECT EXPAND(in) FROM `connect` WHERE out.ruleName=?";
			query += " AND out.occurTime - ? <= in.managerTime";
			params = new Object[] { ruleName, timeRange };
		} else {
			// long maxTime = (long) (new Date()).getTime() / 1000;
			// long minTime = maxTime - timeRange;
			query = "SELECT EXPAND($all) LET $in = (SELECT EXPAND(in) FROM `connect` WHERE out.ruleName=? AND out.occurTime - ? <= in.managerTime), "
					+ "$out=(SELECT EXPAND(out) FROM `connect` WHERE out.ruleName=? AND out.occurTime - ? <= in.managerTime), $all=UNIONALL($in,$out)";
			params = new Object[] { ruleName, timeRange, ruleName, timeRange };
		}

		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query, params);
		if (result != null) {
			while (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				list.add(Converter.toEvent(obj));
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

	@SuppressWarnings("unchecked")
	public static List<OrientVertex> getAllAlerts() {
		List<OrientVertex> list = new ArrayList<OrientVertex>();
		String query = "SELECT FROM Alert";
		Iterable<Object> result = (Iterable<Object>) OrientDBFactory.executeQuery(query, null);
		if (result != null) {
			while (result.iterator().hasNext()) {
				OrientVertex obj = (OrientVertex) result.iterator().next();
				list.add(obj);
			}
		}
		return list;
	}
}
