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
package uk.co.alt236.floatinginfo.provider.generalinfo.asynctask;

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
