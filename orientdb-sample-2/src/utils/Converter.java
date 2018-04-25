package utils;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import models.Event;

public class Converter {
	public static Event toEvent(OrientVertex v) {
		Event result = new Event();
		result.setDestinationIP(v.getProperty("destinationIP"));
		result.setDestinationPort(v.getProperty("destinationPort"));
		result.setLogType(v.getProperty("logType"));
		result.setSourceIP(v.getProperty("sourceIP"));
		result.setSourcePort(v.getProperty("sourcePort"));
		result.setProduct(v.getProperty("product"));
		result.setMethod(v.getProperty("method"));
		result.setId(v.getProperty("id"));
		result.setOccurTime(v.getProperty("occurTime"));
		result.setEndTime(v.getProperty("endTime"));
		result.setManagerTime(v.getProperty("managerTime"));
		result.setOrigin(v.getProperty("origin"));
		result.setLogSourceName(v.getProperty("logSourceName"));
		result.setLogSourceIP(v.getProperty("logSourceIP"));

		result.setLogTypeMethod(v.getProperty("logType") + ":" + v.getProperty("method"));
		return result;
	}
	

}
