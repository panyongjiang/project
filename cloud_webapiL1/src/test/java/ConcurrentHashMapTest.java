import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentHashMapTest {
public static void main(String[] args) {
	ConcurrentMap concurrentMap =  (ConcurrentMap) new ConcurrentHashMap();
	concurrentMap.put("key", "value");

	Object value = concurrentMap.get("key");
	System.out.println(value);
	ConcurrentNavigableMap<String, String> map = new ConcurrentSkipListMap();
	map.put("1", "one");
	map.put("2", "two");
	map.put("3", "three");

	ConcurrentNavigableMap headMap = map.headMap("3");
	ConcurrentNavigableMap<String, String> tailMap = map.tailMap("2");
	ConcurrentNavigableMap<String, String> subMap = map.subMap("2","3");//取范围中等于最小值以上
	System.out.println(tailMap);
	System.out.println(headMap);
	System.out.println(subMap);
}
}
