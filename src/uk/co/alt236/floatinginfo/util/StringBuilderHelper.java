package uk.co.alt236.floatinginfo.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Pair;
import android.widget.TextView;

public class StringBuilderHelper {
	private static final String BULLET_CHARACTER = Character.toString('\u2022');
	private final static String NEW_LINE = System.getProperty("line.separator");
	private final static String SECTION_LINE = "------------------------------";

	private boolean mKeyValueSectionEntryEnabled;
	private final List<Pair<String, String>> mKeyValueSectionItems = new ArrayList<Pair<String, String>>();
	private int mPadding = 10;
	private final SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

	public void append(int value) {
		append(String.valueOf(value));
	}

	private void append(int padding, String label, String value){
		mStringBuilder.append(' ');
		mStringBuilder.append(BULLET_CHARACTER);
		mStringBuilder.append(padRight(label, padding));
		mStringBuilder.append(": ");
		mStringBuilder.append(value == null ? "null" : value);
		appendNewLine();
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
		if(mKeyValueSectionEntryEnabled){
			mKeyValueSectionItems.add(new Pair<String, String>(label, value));
		} else {
			append(mPadding, label, value);
		}
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

	public void endKeyValueSection(){
		mKeyValueSectionEntryEnabled = false;
		int sectionPadding = 0;

		for(final Pair<String, String> pair : mKeyValueSectionItems){
			if(sectionPadding < pair.first.length()){
				sectionPadding = pair.first.length();
			}
		}

		for(final Pair<String, String> pair : mKeyValueSectionItems){
			append(sectionPadding, pair.first, pair.second);
		}

		mKeyValueSectionItems.clear();
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

	public void startKeyValueSection(){
		mKeyValueSectionEntryEnabled = true;
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
