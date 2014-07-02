package uk.co.alt236.floatinginfo.provider.generalinfo.ui;

import java.util.List;

import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.util.PeakKeeper;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import uk.co.alt236.floatinginfo.util.Util;
import android.view.View;
import android.widget.TextView;

public class UiUpdater {
	private final InfoStore mInfoStore;
	private final PeakKeeper mPeakKeeper;

	public UiUpdater(InfoStore store) {
		mPeakKeeper = new PeakKeeper();
		mInfoStore = store;
	}

	public void clearPeakUsage() {
		mPeakKeeper.clear();
	}

	private void constructMemoryLine(StringBuilderHelper sb, String key, int value) {
		if (value >= 0) { // We assume negative values mean that something went
							// wrong with reflection.
			String valueStr = Util.getHumanReadableKiloByteCount(value, true);
			mPeakKeeper.update(key, value);
			final int peak = mPeakKeeper.getValue(key);
			if (peak > 0) {
				valueStr += " (Peak: " + Util.getHumanReadableKiloByteCount(peak, true) + ")";
			}

			sb.append(key, valueStr);
		}
	}

	private CharSequence getInfoText() {
		final StringBuilderHelper sb = new StringBuilderHelper();

		if (mInfoStore != null) {
			final ForegroundProcessInfo procInfo = mInfoStore.getForegroundProcessInfo();
			if (procInfo != null) {
				sb.appendBold("Foreground Application Info");
				sb.startKeyValueSection();
				sb.append("App Name", String.valueOf(procInfo.getAppName()));
				sb.append("Package", procInfo.getPackage());
				sb.append("PID", procInfo.getPid());
				sb.endKeyValueSection();
				sb.appendNewLine();
			}

			final CpuData cpuInfo = mInfoStore.getCpuInfo();

			if (cpuInfo != null) {
				sb.appendBold("Global CPU Utilisation");
				sb.startKeyValueSection();
				sb.append("Total", String.valueOf(cpuInfo.getOveralCpu()) + "%");
				final List<Integer> list = cpuInfo.getPerCpuUtilisation();

				int count = 0;

				for (Integer value : list) {
					sb.append("CPU" + count, String.valueOf(value) + "%");
					count++;
				}
				sb.endKeyValueSection();
				sb.appendNewLine();
			}

			final MemoryData memoryInfo = mInfoStore.getMemoryInfo();

			if (memoryInfo != null) {
				sb.appendBold("Current Process Memory Utilisation");
				sb.startKeyValueSection();
				constructMemoryLine(
						sb,
						"DalvikPrivateClean",
						memoryInfo.getDalvikPrivateClean());
				constructMemoryLine(
						sb,
						"DalvikPrivateDirty",
						memoryInfo.getDalvikPrivateDirty());
				constructMemoryLine(
						sb,
						"DalvikPss",
						memoryInfo.getDalvikPss());
				constructMemoryLine(
						sb,
						"DalvikSharedClean",
						memoryInfo.getDalvikSharedClean());
				constructMemoryLine(
						sb,
						"DalvikSharedDirty",
						memoryInfo.getDalvikSharedDirty());
				constructMemoryLine(
						sb,
						"DalvikSwappablePss",
						memoryInfo.getDalvikSwappablePss());
				constructMemoryLine(
						sb,
						"DalvikSwappedOut",
						memoryInfo.getDalvikSwappedOut());

				constructMemoryLine(
						sb,
						"NativePrivateClean",
						memoryInfo.getNativePrivateClean());
				constructMemoryLine(
						sb,
						"NativePrivateDirty",
						memoryInfo.getNativePrivateDirty());
				constructMemoryLine(
						sb,
						"NativePss",
						memoryInfo.getNativePss());
				constructMemoryLine(
						sb,
						"NativeSharedClean",
						memoryInfo.getNativeSharedClean());
				constructMemoryLine(
						sb,
						"NativeSharedDirty",
						memoryInfo.getNativeSharedDirty());
				constructMemoryLine(
						sb,
						"NativeSwappablePss",
						memoryInfo.getNativeSwappablePss());
				constructMemoryLine(
						sb,
						"NativeSwappedOut",
						memoryInfo.getNativeSwappedOut());

				constructMemoryLine(
						sb,
						"OtherPrivateClean",
						memoryInfo.getOtherPrivateClean());
				constructMemoryLine(
						sb,
						"OtherPrivateDirty",
						memoryInfo.getOtherPrivateDirty());
				constructMemoryLine(
						sb,
						"OtherPss",
						memoryInfo.getOtherPss());
				constructMemoryLine(
						sb,
						"OtherSharedClean",
						memoryInfo.getOtherSharedClean());
				constructMemoryLine(
						sb,
						"OtherSharedDirty",
						memoryInfo.getOtherSharedDirty());
				constructMemoryLine(
						sb,
						"OtherSwappablePss",
						memoryInfo.getOtherSwappablePss());
				constructMemoryLine(
						sb,
						"OtherSwappedOut",
						memoryInfo.getOtherSwappedOut());

				sb.endKeyValueSection();
				sb.appendNewLine();
			}
		}

		return sb.toCharSequence();
	}

	public CharSequence getSharePayload() {
		return getInfoText();
	}

	public void update(View view) {
		((TextView) view).setText(getInfoText());
	}
}
