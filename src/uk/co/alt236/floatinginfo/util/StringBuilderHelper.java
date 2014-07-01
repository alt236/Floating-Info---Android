package uk.co.alt236.floatinginfo.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class StringBuilderHelper {
	private static final String BULLET_CHARACTER = Character.toString('\u2022');
	private final static String NEW_LINE = System.getProperty("line.separator");
	private final static String SECTION_LINE = "------------------------------";
	private int mPadding = 10;

	private final SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

	public void append(int value) {
		append(String.valueOf(value));
	}

	public void append(String value) {
		mStringBuilder.append(value);
		appendNewLine();
	}

	public void append(String label, boolean value) {
		append(label, String.valueOf(value));
	}

	public void append(String label, float value) {
		append(label, String.valueOf(value));
	}

	public void append(String label, long value) {
		append(label, String.valueOf(value));
	}

	public void append(String label, String value) {
		mStringBuilder.append(' ');
		mStringBuilder.append(BULLET_CHARACTER);
		mStringBuilder.append(padRight(label, mPadding));
		mStringBuilder.append(":\t");
		mStringBuilder.append(value == null ? "null" : value);
		appendNewLine();
	}

	public void appendBold(String value) {
		final SpannableString ss = new SpannableString(value);
		ss.setSpan(
				new StyleSpan(Typeface.BOLD),
				0,
				ss.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		mStringBuilder.append(ss);
		appendNewLine();
	}

	public void appendNewLine() {
		mStringBuilder.append(NEW_LINE);
	}

	public void appendSectionLine() {
		mStringBuilder.append(SECTION_LINE);
		appendNewLine();
	}

	public void appendWithValueAsNewLine(String label, String value) {
		append(label, NEW_LINE + padRight("", 5) + value);
	}

	public int getPadding(){
		return mPadding;
	}

	public void setPadding(int padding){
		mPadding = padding;
	}

	public void setTextToView(TextView view){
		view.setText(mStringBuilder);
	}

	public CharSequence toCharSequence(){
		return mStringBuilder;
	}

	@Override
	public String toString(){
		return mStringBuilder.toString();
	}

	private static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
}
