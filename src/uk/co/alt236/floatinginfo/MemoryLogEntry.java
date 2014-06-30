package uk.co.alt236.floatinginfo;

public final class MemoryLogEntry {
	private final long mPid;
	private final String mAppName;
	private final long mTimestamp;

	public MemoryLogEntry(long timestamp, long pid, String appName){
		mPid = pid;
		mTimestamp = timestamp;
		mAppName = appName;
	}

	public long getPid() {
		return mPid;
	}

	public String getAppName() {
		return mAppName;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	@Override
	public String toString() {
		return "MemoryLogEntry [mPid=" + mPid + ", mAppName=" + mAppName + ", mTimestamp=" + mTimestamp + "]";
	}


}
