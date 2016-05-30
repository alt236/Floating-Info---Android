package uk.co.alt236.floatinginfo.overlay;

import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ interface TextWriter<T> {
    void writeText(final T input, final StringBuilderHelper stringBuilder);

    void clear();
}
