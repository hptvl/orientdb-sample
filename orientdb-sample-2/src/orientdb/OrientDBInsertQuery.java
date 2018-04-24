package orientdb;

import java.util.Map;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * An implementation of data inserting queries on OrientDB
 * 
 * @author Tran Ho
 *
 */
public class OrientDBInsertQuery {
	/**
	 * Insert new vertex to orientDB graph with its own properties
	 * 
	 * 
	 * @param className
	 *            a vertex class is pre-defined in OrientDB schema
	 * @param properties
	 *            a vertex's properties set
	 * @return a new vertex
	 */
	public static OrientVertex insertVertex(String className, Map<String, Object> properties) {
		return OrientDBFactory.insertVertex(className, properties);
	}

	/**
	 * Insert new edge (relation) between two vertices to orientDB graph with its
	 * own properties
	 * 
	 * 
	 * @param className
	 *            a edge class is pre-defined in OrientDB schema
	 * @param from
	 *            From vertex
	 * @param to
	 *            To vertex
	 * @return new edge
	 */
	public static OrientEdge insertEdge(String className, OrientVertex from, OrientVertex to) {
		return OrientDBFactory.insertEdge(className, from, to);
	}

}
