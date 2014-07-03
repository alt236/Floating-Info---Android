/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory;

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
