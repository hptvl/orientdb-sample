package parsing;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.orientechnologies.orient.core.sql.parser.ParseException;

import models.Event;
import utils.ESMOntNetworkUtil;

public class JsonParser {
	@SuppressWarnings("unchecked")
	public JSONObject createOntologyJson(List<Event> events) throws ParseException, UnsupportedEncodingException {
		if (events == null || events.size() <= 0) {
			return null;
		}

		Map<String, List<Event>> groupLogTypeMethod = events.stream()
				.collect(Collectors.groupingBy(Event::getLogTypeMethod));
		Map<Long, List<Event>> groupSourceIP = events.stream().collect(Collectors.groupingBy(Event::getSourceIP));
		Map<Long, List<Event>> groupDestinationIP = events.stream()
				.collect(Collectors.groupingBy(Event::getDestinationIP));
		// STEP 3.
		List<String> logTypeList = new ArrayList<>();
		List<String> nodeNameList = new ArrayList<>();
		List<String> nodeDisplayList = new ArrayList<>();
		List<Integer> nodeCountList = new ArrayList<>();

		for (String key : groupLogTypeMethod.keySet()) {
			String[] keys = key.split(":");
			String display_en = "[" + keys[0] + "]" + keys[1];

			logTypeList.add(keys[0]);
			nodeNameList.add("method");
			nodeDisplayList.add(display_en);
			nodeCountList.add(groupLogTypeMethod.get(key).size());
		}
		for (Long key : groupSourceIP.keySet()) {
			logTypeList.add("");
			nodeNameList.add("sIP");
			nodeDisplayList.add(ESMOntNetworkUtil.convertIPToDotted(key));
			nodeCountList.add(groupSourceIP.get(key).size());
		}

		for (Long key : groupDestinationIP.keySet()) {
			logTypeList.add("");
			nodeNameList.add("dIP");
			nodeDisplayList.add(ESMOntNetworkUtil.convertIPToDotted(key));
			nodeCountList.add(groupDestinationIP.get(key).size());
		}
		// STEP 4. nodes 만들기
		JSONArray nodesArray = new JSONArray();
		for (int i = 0; i < nodeNameList.size(); i++) {
			JSONObject node = new JSONObject();

			// 색상표 : liryka:epika:dramat = R:G:B
			switch (nodeNameList.get(i)) {
			case "method":
				if (logTypeList.get(i).equals("ESM")) {
					node.put("liryka", 255);
					node.put("epika", 72);
					node.put("dramat", 30);
				} else if (logTypeList.get(i).equals("IPS")) {
					node.put("liryka", 50);
					node.put("epika", 68);
					node.put("dramat", 242);
				} else if (logTypeList.get(i).equals("WAF")) {
					node.put("liryka", 34);
					node.put("epika", 231);
					node.put("dramat", 34);
				} else {
					node.put("liryka", 192);
					node.put("epika", 179);
					node.put("dramat", 212);
				}
				break;
			case "sIP":
				node.put("liryka", 177);
				node.put("epika", 200);
				node.put("dramat", 230);
				break;
			case "dIP":
				node.put("liryka", 177);
				node.put("epika", 200);
				node.put("dramat", 230);
				break;
			default:
				node.put("liryka", 307);
				node.put("epika", 1187);
				node.put("dramat", 201);
				break;
			}

			node.put("count", getCountVal(nodeCountList.get(i))); // 노드 크기, nodeCount.get(i)*100
			node.put("name", nodeNameList.get(i));
			node.put("display", String.valueOf(i));
			node.put("display_en", nodeDisplayList.get(i)); // 화면표시 이름

			nodesArray.add(node);
		}

		// STEP 5. links 만들기
		JSONArray linksArray = new JSONArray();
		for (Long key : groupSourceIP.keySet()) {
			List<Event> tempList = groupSourceIP.get(key);
			int sourceIdx = getIdx("sIP", nodeNameList, ESMOntNetworkUtil.convertIPToDotted(key), nodeDisplayList);

			JSONObject link = new JSONObject();
			link.put("count", nodeCountList.get(sourceIdx) * 500); // 노드 크기
			link.put("strength", 1.5); // 선굵기
			link.put("source", sourceIdx); // 자신 노드 인덱스
			link.put("target", 0);
			linksArray.add(link);

			Map<String, List<Event>> tempMap = tempList.stream()
					.collect(Collectors.groupingBy(Event::getLogTypeMethod));
			for (String logTypeMethod : tempMap.keySet()) {
				String[] keys = logTypeMethod.split(":");
				String display_en = "[" + keys[0] + "]" + keys[1];

				int targetIdx = nodeDisplayList.indexOf(display_en);
				if (targetIdx == -1)
					targetIdx = sourceIdx;

				link = new JSONObject();
				link.put("count", nodeCountList.get(sourceIdx) * 500);
				link.put("strength", 1.5);
				link.put("source", sourceIdx);
				link.put("target", targetIdx);
				linksArray.add(link);
			}
		}

		for (Long key : groupDestinationIP.keySet()) {
			List<Event> tempList = groupDestinationIP.get(key);
			int sourceIdx = getIdx("dIP", nodeNameList, ESMOntNetworkUtil.convertIPToDotted(key), nodeDisplayList);

			JSONObject link = new JSONObject();
			link.put("count", nodeCountList.get(sourceIdx) * 500);
			link.put("strength", 1.5);
			link.put("source", sourceIdx);
			link.put("target", 0);
			linksArray.add(link);

			Map<String, List<Event>> tempMap = tempList.stream()
					.collect(Collectors.groupingBy(Event::getLogTypeMethod));
			for (String logTypeMethod : tempMap.keySet()) {
				String[] keys = logTypeMethod.split(":");
				String display_en = "[" + keys[0] + "]" + keys[1];

				int targetIdx = nodeDisplayList.indexOf(display_en);
				if (targetIdx == -1)
					targetIdx = sourceIdx;

				link = new JSONObject();
				link.put("count", nodeCountList.get(sourceIdx) * 500);
				link.put("strength", 1.5);
				link.put("source", sourceIdx);
				link.put("target", targetIdx);
				linksArray.add(link);
			}
		}

		JSONObject result = new JSONObject();
		result.put("nodes", nodesArray);
		result.put("links", linksArray);

		return result;
	}

	private int getCountVal(int count) {
		int val = 500;
		if (count < 0) {
			val = 0;
		} else if (count <= 10) {
			val = 500;
		} else if (count <= 20) {
			val = 1500;
		} else {
			val = 2500;
		}
		return val;
	}

	private int getIdx(String nodeName, List<String> nodeNameList, String nodeDisplay, List<String> nodeDisplayList) {
		for (int i = 0; i < nodeNameList.size(); i++) {
			if (nodeName.equals(nodeNameList.get(i)) && nodeDisplay.equals(nodeDisplayList.get(i))) {
				return i;
			}
		}
		return -1;
	}
}