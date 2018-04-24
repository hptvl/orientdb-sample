package orientdb;

import java.util.Map;

import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class OrientDBFactory {
	public static final int MAX_INSERT_TRANSACTION = 8000;
	// private static OrientGraph graphTx;
	// private static OrientGraphNoTx graphNoTx;
	private static OrientBaseGraph graph;
	private static OrientGraphFactory factory;

	private OrientDBFactory() {

	}

	public static void init(String url, String userName, String pwd, int minPoolSize, int maxPoolSize,
			boolean isTransactionUsed) {
		if (factory != null) {
			factory.close();
		}
		factory = new OrientGraphFactory(url, userName, pwd).setupPool(minPoolSize, maxPoolSize);
		graph = isTransactionUsed ? factory.getTx() : factory.getNoTx();

	}

	public static Object executeQuery(String query) {
		OCommandSQL sql = new OCommandSQL(query);
		return graph.command(sql).execute();
	}

	public static OrientVertex insertVertex(String className, Map<String, Object> properties) {
		return graph.addVertex("class:" + className, properties);
	}

	public static OrientEdge insertEdge(String className, OrientVertex from, OrientVertex to) {
		return graph.addEdge("class:" + className, from, to, null);
	}

	public static void close() {
		if (graph != null && !graph.isClosed()) {
			graph.shutdown();
		}

		if (factory != null) {
			factory.close();
		}
	}

	public static void setStandardElementConstraints(boolean flag) {
		graph.setStandardElementConstraints(flag);
	}

	public static void setUseLightweightEdges(boolean flag) {
		graph.setUseLightweightEdges(flag);
	}

	public static void setMassiveInsert(boolean flag) {
		graph.declareIntent(flag ? new OIntentMassiveInsert() : null);
	}
}
