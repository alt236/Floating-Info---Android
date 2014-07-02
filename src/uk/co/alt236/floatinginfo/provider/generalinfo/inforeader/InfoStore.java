package uk.co.alt236.floatinginfo.provider.generalinfo.inforeader;

import java.util.HashMap;
import java.util.Map;

import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryData;

public class InfoStore {
	public final Object mLock = new Object();

	private final Map<Key, Object> mStore = new HashMap<Key, Object>();

	private Object get(Key key){
		synchronized (mLock) {
			return mStore.get(key);
		}
	}

	public CpuData getCpuInfo(){
		return (CpuData) get(Key.CPU_INFO);
	}

	public ForegroundProcessInfo getForegroundProcessInfo(){
		return (ForegroundProcessInfo) get(Key.PROCESS_INFO);
	}

	public MemoryData getMemoryInfo(){
		return (MemoryData) get(Key.MEMORY_INFO);
	}

	private void put(Key key, Object value){
		synchronized (mLock) {
			mStore.put(key, value);
		}
	}

	public void set(CpuData value) {
		put(Key.CPU_INFO, value);
	}

	public void set(ForegroundProcessInfo value){
		put(Key.PROCESS_INFO, value);
	}

	public void set(MemoryData value){
		put(Key.MEMORY_INFO, value);
	}

	private enum Key{
		PROCESS_INFO,
		CPU_INFO,
		MEMORY_INFO
	}
}
