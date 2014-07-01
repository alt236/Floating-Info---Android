package uk.co.alt236.floatinginfo.reader;

import uk.co.alt236.floatinginfo.container.MemoryData;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;

public class MemoryInfoReader {
	private MemoryData mMemoryInfo;
	private ActivityManager mActivityManager;

	public MemoryInfoReader(Context context){
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}


	public void update(int pid) {
		final MemoryInfo mi = mActivityManager.getProcessMemoryInfo(new int[]{pid})[0];

		mMemoryInfo = new MemoryData(mi);

	}

	public MemoryData getInfo(){
		return mMemoryInfo;
	}
}
