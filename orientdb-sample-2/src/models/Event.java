package models;

import java.util.Objects;

public class Event {
	private String key; // Alert, SourceIP

	private Long destinationIP;
	private Integer destinationPort;
	private String logType;
	private Integer sourcePort;
	private String product;
	private Long sourceIP;
	private String method;
	private Long occurTime;
	private String origin;
	private String id;
	private String logSourceName;
	private Long endTime;
	private String logSourceIP;
	private String event;
	private Long managerTime;

	private String logTypeMethod;

	public Event(String logTypeMethod, String method, Long sourceIP, Long destinationIP) {
		this.logTypeMethod = logTypeMethod;
		this.method = method;
		this.sourceIP = sourceIP;
		this.destinationIP = destinationIP;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(Long destinationIP) {
		this.destinationIP = destinationIP;
	}

	public Integer getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(Integer destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public Integer getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Long getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(Long sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Long occurTime) {
		this.occurTime = occurTime;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogSourceName() {
		return logSourceName;
	}

	public void setLogSourceName(String logSourceName) {
		this.logSourceName = logSourceName;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getLogSourceIP() {
		return logSourceIP;
	}

	public void setLogSourceIP(String logSourceIP) {
		this.logSourceIP = logSourceIP;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Long getManagerTime() {
		return managerTime;
	}

	public void setManagerTime(Long managerTime) {
		this.managerTime = managerTime;
	}

	public String getLogTypeMethod() {
		return logTypeMethod;
	}

	public void setLogTypeMethod(String logTypeMethod) {
		this.logTypeMethod = logTypeMethod;
	}

	@Override
	public String toString() {
		return "Ontology [key=" + key + ", destinationIP=" + destinationIP + ", destinationPort=" + destinationPort
				+ ", logType=" + logType + ", sourcePort=" + sourcePort + ", product=" + product + ", sourceIP="
				+ sourceIP + ", method=" + method + ", occurTime=" + occurTime + ", origin=" + origin + ", id=" + id
				+ ", logSourceName=" + logSourceName + ", endTime=" + endTime + ", logSourceIP=" + logSourceIP
				+ ", event=" + event + ", managerTime=" + managerTime + ", logTypeMethod=" + logTypeMethod + "]";
	}

	@Override
	public boolean equals(Object _obj) {
		if (_obj == null)
			return false;
		if (getClass() != _obj.getClass())
			return false;

		final Event obj = (Event) _obj;
		if (Objects.equals(this.key, obj.key))
			return false;
		if (Objects.equals(this.logType, obj.logType))
			return false;
		if (Objects.equals(this.method, obj.method))
			return false;
		if (Objects.equals(this.sourceIP, obj.sourceIP))
			return false;
		// if(Objects.equal(this.sourcePort, obj.sourcePort)) return false;
		if (Objects.equals(this.destinationIP, obj.destinationIP))
			return false;
		if (Objects.equals(this.destinationPort, obj.destinationPort))
			return false;

		if (Objects.equals(this.logTypeMethod, obj.logTypeMethod))
			return false;

		return true;
	}

	public Event() {
	}

}