package uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuData {
	private final List<Integer> mPerCpuUtilisation;
	private final int mOveralCpu;

	public CpuData(int overalCpu){
		mPerCpuUtilisation = new ArrayList<Integer>();
		mOveralCpu = overalCpu;
	}

	public void addCpuUtil(int value){
		mPerCpuUtilisation.add(value);
	}

	public List<Integer> getPerCpuUtilisation() {
		return Collections.unmodifiableList(mPerCpuUtilisation);
	}

	public int getOveralCpu() {
		return mOveralCpu;
	}
}
