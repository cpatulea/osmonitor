package com.eolwral.osmonitor;

import java.io.IOException;
import java.io.Reader;

public class IOUtils {
	/**
	 * Read entire contents of a reader.
	 *
	 * TODO: There should be a character limit here to prevent memory blowups.
	 *
	 * @param r reader to read from
	 * @return the characters read
	 * @throws IOException
	 */
	public static String readAll(Reader r) throws IOException {
		// Choose this value such that most readers are less than this many
		// characters long. If the particular reader given satisfies this
		// condition, this function will only make two calls to read(), one to
		// fill the buffer, and another to detect EOF.
		final int BUFFER_SIZE_CHARS = 256;

		char[] buffer = new char[BUFFER_SIZE_CHARS];
		int numRead;

		// Slow path: accumulate into a StringBuilder.
		StringBuilder b = new StringBuilder();
		while ((numRead = r.read(buffer)) != -1) {
			b.append(buffer, 0, numRead);
		}

		return b.toString();
	}

	/**
	 * Read entire contents of a @code{Reader}, but return only the contents
	 * up to, but not including, the first null (\0) character.
	 *
	 * @param r reader to read from
	 * @return the characters read, not including the null terminator
	 * @throws IOException
	 */
	public static String readUpToNull(Reader r) throws IOException {
		String contents = readAll(r);
		int indexOfNull = contents.indexOf('\0');
		if (indexOfNull == -1) {
			return contents;
		} else {
			return contents.substring(0, indexOfNull);
		}
	}
}
