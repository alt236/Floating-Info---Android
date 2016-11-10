/*
 * Copyright 2016 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.alt236.floatinginfo.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Pair;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StringBuilderHelper {
    private static final String BULLET_CHARACTER = Character.toString('\u2022');
    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static String SECTION_LINE = "------------------------------";
    private final List<Pair<String, String>> mKeyValueSectionItems = new ArrayList<Pair<String, String>>();
    private final SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
    private boolean mKeyValueSectionEntryEnabled;
    private int mPadding = 10;

    private static String padRight(final String s, final int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public void append(final int value) {
        append(String.valueOf(value));
    }

    private void append(final int padding, final String label, final String value) {
        mStringBuilder.append(' ');
        mStringBuilder.append(BULLET_CHARACTER);
        mStringBuilder.append(padRight(label, padding));
        mStringBuilder.append(": ");
        mStringBuilder.append(value == null ? "null" : value);
        appendNewLine();
    }

    public void append(final String value) {
        mStringBuilder.append(value);
        appendNewLine();
    }

    public void append(final String label, final boolean value) {
        append(label, String.valueOf(value));
    }

    public void append(final String label, final float value) {
        append(label, String.valueOf(value));
    }

    public void append(final String label, final long value) {
        append(label, String.valueOf(value));
    }

    public void append(final String label, final String value) {
        if (mKeyValueSectionEntryEnabled) {
            mKeyValueSectionItems.add(new Pair<String, String>(label, value));
        } else {
            append(mPadding, label, value);
        }
    }

    public void appendBold(final String value) {
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

    public void appendWithValueAsNewLine(final String label, final String value) {
        append(label, NEW_LINE + padRight("", 5) + value);
    }

    public void endKeyValueSection() {
        mKeyValueSectionEntryEnabled = false;
        int sectionPadding = 0;

        for (final Pair<String, String> pair : mKeyValueSectionItems) {
            if (sectionPadding < pair.first.length()) {
                sectionPadding = pair.first.length();
            }
        }

        for (final Pair<String, String> pair : mKeyValueSectionItems) {
            append(sectionPadding, pair.first, pair.second);
        }

        mKeyValueSectionItems.clear();
    }

    public int getPadding() {
        return mPadding;
    }

    public void setPadding(final int padding) {
        mPadding = padding;
    }

    public void setTextToView(final TextView view) {
        view.setText(mStringBuilder);
    }

    public void startKeyValueSection() {
        mKeyValueSectionEntryEnabled = true;
    }

    public CharSequence toCharSequence() {
        return mStringBuilder;
    }

    @Override
    public String toString() {
        return mStringBuilder.toString();
    }
}
