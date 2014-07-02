
package uk.co.alt236.floatinginfo.util;

import java.util.Locale;

public class Util {


	public static String getHumanReadableKiloByteCount(long kbytes, boolean si) {
		return getHumanReadableByteCount(kbytes * 1024, si);
	}

	public static String getHumanReadableByteCount(long bytes, boolean si) {
		final int unit = si ? 1000 : 1024;

		if (bytes < unit){
			return bytes + " B";
		}

		final int exp = (int) (Math.log(bytes) / Math.log(unit));
		final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");

		return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
