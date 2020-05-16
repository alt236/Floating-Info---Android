/*
 * Copyright 2020 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.common.string

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Pair
import android.widget.TextView
import java.util.*

@Suppress("unused")
class StringBuilderHelper {
    private val mKeyValueSectionItems: MutableList<Pair<String, String>> = ArrayList()
    private val mStringBuilder = SpannableStringBuilder()
    private var mKeyValueSectionEntryEnabled = false
    var padding = 10
    fun append(value: Int) {
        append(value.toString())
    }

    private fun append(padding: Int, label: String, value: String?) {
        mStringBuilder.append(' ')
        mStringBuilder.append(BULLET_CHARACTER)
        mStringBuilder.append(padRight(label, padding))
        mStringBuilder.append(": ")
        mStringBuilder.append(value ?: "null")
        appendNewLine()
    }

    fun append(value: String?) {
        mStringBuilder.append(value)
        appendNewLine()
    }

    fun append(label: String, value: Boolean) {
        append(label, value.toString())
    }

    fun append(label: String, value: Float) {
        append(label, value.toString())
    }

    fun append(label: String, value: Long) {
        append(label, value.toString())
    }

    fun append(label: String, value: String) {
        if (mKeyValueSectionEntryEnabled) {
            mKeyValueSectionItems.add(Pair(label, value))
        } else {
            append(padding, label, value)
        }
    }

    fun appendBold(value: String?) {
        val ss = SpannableString(value)
        ss.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                ss.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mStringBuilder.append(ss)
        appendNewLine()
    }

    fun appendNewLine() {
        mStringBuilder.append(NEW_LINE)
    }

    fun appendSectionLine() {
        mStringBuilder.append(SECTION_LINE)
        appendNewLine()
    }

    fun appendWithValueAsNewLine(label: String, value: String) {
        append(label, NEW_LINE + padRight("", 5) + value)
    }

    fun endKeyValueSection() {
        mKeyValueSectionEntryEnabled = false
        var sectionPadding = 0
        for (pair in mKeyValueSectionItems) {
            if (sectionPadding < pair.first.length) {
                sectionPadding = pair.first.length
            }
        }
        for (pair in mKeyValueSectionItems) {
            append(sectionPadding, pair.first, pair.second)
        }
        mKeyValueSectionItems.clear()
    }

    fun setTextToView(view: TextView) {
        view.text = mStringBuilder
    }

    fun startKeyValueSection() {
        mKeyValueSectionEntryEnabled = true
    }

    fun toCharSequence(): CharSequence {
        return mStringBuilder
    }

    override fun toString(): String {
        return mStringBuilder.toString()
    }

    companion object {
        private const val SECTION_LINE = "------------------------------"
        private const val BULLET_CHARACTER = '\u2022'.toString()
        private val NEW_LINE = System.getProperty("line.separator") ?: "\n"

        private fun padRight(s: String, n: Int): String {
            return String.format("%1$-" + n + "s", s)
        }
    }
}