package vaccumExt;

import java.util.HashSet;
import java.util.Set;

public class MyVacuumEnvironment extends GenericVacuumEnvironment<Integer> {
	
	public static final Integer[] LOCATION_MAP = {0,1,2,3,4,5,6,7};
	
	public MyVacuumEnvironment() {	
		super(getKeySet());
	}

	public static Set<Integer> getKeySet() {
		Set<Integer> keys = new HashSet<Integer>();
		for(Integer i : LOCATION_MAP)
			keys.add(i);
		
		return keys;
	}
}
