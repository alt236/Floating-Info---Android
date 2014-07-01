package uk.co.alt236.floatinginfo.container;

public class ForegroundProcessInfo {
	private final int mPid;
	private final String mPackage;
	private final CharSequence mAppName;

	public ForegroundProcessInfo(
			int pid,
			String pkg,
			CharSequence appName) {

		mPid = pid;
		mPackage = pkg;
		mAppName = appName;
	}

	public int getPid() {
		return mPid;
	}

	public String getPackage() {
		return mPackage;
	}

	public CharSequence getAppName(){
		return mAppName;
	}

	@Override
	public String toString() {
		return "ForegroundProcessInfo [mPid=" + mPid + ", mPackage=" + mPackage + ", mAppName=" + mAppName + "]";
	}


}