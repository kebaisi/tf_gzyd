package com.kbs.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class pp {
	public static void main(String[] args) {
		 Map<String, Integer> map = new TreeMap<String, Integer>();
for(int i=0;i<10000;i++){
	map.put("60003"+i, i);
	map.put("60003"+i, i);
	map.put("60003"+i, i);
}
		Long tem = System.currentTimeMillis();
//		Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
		 Map<String, Integer> resultMap = sortMapByValue(map); //按Value进行排序


        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("elpsed："+(System.currentTimeMillis()-tem));
		
	}
	public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

	  
}
class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Entry<String, Integer> me1, Entry<String, Integer> me2) {

//        return me1.getValue().compareTo(me2.getValue());
    	return me2.getValue().compareTo(me1.getValue());
    }
}
