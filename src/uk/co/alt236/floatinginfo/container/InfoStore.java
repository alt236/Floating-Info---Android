package uk.co.alt236.floatinginfo.container;

import java.util.HashMap;
import java.util.Map;

public class InfoStore {
	public final Object mLock = new Object();

	private final Map<Key, Object> mStore = new HashMap<Key, Object>();

	private Object get(Key key){
		synchronized (mLock) {
			return mStore.get(key);
		}
	}

	public ForegroundProcessInfo getForegroundProcessInfo(){
		return (ForegroundProcessInfo) get(Key.PROCESS_INFO);
	}

	private void put(Key key, Object value){
		synchronized (mLock) {
			mStore.put(key, value);
		}
	}

	public void set(ForegroundProcessInfo value){
		put(Key.PROCESS_INFO, value);
	}

	private enum Key{
		PROCESS_INFO,
		CPU_INFO
	}
}
