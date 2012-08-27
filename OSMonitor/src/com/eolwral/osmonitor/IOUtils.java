package com.eolwral.osmonitor;

import java.io.IOException;
import java.io.Reader;

public class IOUtils {
	/**
	 * Read from a @code{Reader} up to a null (\0) character.
	 * 
	 * @param r reader to read from
	 * @return the characters read, not including the null terminator
	 * @throws IOException
	 */
	public static String readUpToNull(Reader r) throws IOException {
		// Process names shorter than this will take a fast path in this
		// function. Choose a value large enough such that most process names
		// are shorter than this many characters, but not too large.
		final int LONG_PROCESS_NAME_LENGTH = 256;

		char[] buffer = new char[LONG_PROCESS_NAME_LENGTH];
		int numRead;
		String strBuffer;

		// Fast path when \0 is in the first 256 bytes of cmdline.
		numRead = r.read(buffer);
		if (numRead == -1) {
			return "";
		}

		strBuffer = new String(buffer, 0, numRead);
		int indexOfNull = strBuffer.indexOf('\0');
		if (indexOfNull != -1) {
			return new String(buffer, 0, indexOfNull);
		}

		// Slow path: accumulate into buffer.
		StringBuilder b = new StringBuilder();
		b.append(buffer, 0, numRead);

		while ((numRead = r.read(buffer)) != -1) {
			strBuffer = new String(buffer, 0, numRead);
			indexOfNull = strBuffer.indexOf('\0');
			if (indexOfNull != -1) {
				b.append(buffer, 0, indexOfNull);
				return b.toString();
			}

			b.append(buffer);
		}

		// Null was missing - return entire cmdline.
		return b.toString();
	}
}
