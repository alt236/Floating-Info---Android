package uk.co.alt236.floatinginfo.util;

import java.util.HashMap;
import java.util.Map;

public class PeakKeeper {

	private final Object mLock = new Object();
	private final Map<String, Integer> mStore = new HashMap<String, Integer>();

	public void clear(){
		synchronized (mLock) {
			mStore.clear();
		}
	}

	public int getValue(String key){
		synchronized (mLock) {
			final Integer res = mStore.get(key);
			if(res == null){
				return 0;
			} else {
				return res.intValue();
			}
		}
	}

	public void update(String key, int value){
		synchronized (mLock) {
			final Integer stored = mStore.get(key);
			final int tmp;

			if(stored == null){
				tmp = -1;
			} else {
				tmp = stored.intValue();
			}

			if(value > tmp){
				mStore.put(key, value);
			}
		}
	}

}
