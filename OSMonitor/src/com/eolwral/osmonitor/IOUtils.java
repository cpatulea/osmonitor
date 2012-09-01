package com.eolwral.osmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
	/**
	 * Read entire contents of an @code{InputStream}.
	 *
	 * TODO: There should be a character limit here to prevent memory blowups.
	 *
	 * @param is stream to read from
	 * @return the characters read
	 * @throws IOException
	 */
	public static String readAll(InputStream is) throws IOException {
		// Choose this value such that most input streams are shorter than this
		// many bytes. If the particular input stream given satisfies this
		// condition, this function will only make two calls to read(), one to
		// fill the buffer, and another to detect EOF.
		final int BUFFER_SIZE_BYTES = 256;

		List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
		int total = 0;
		for (;;) {
			ByteBuffer b = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
			
			int numRead = is.read(b.array());
			if (numRead == -1) {
				break;
			}

			b.limit(numRead);
			buffers.add(b);
			total += numRead;
		}

		ByteBuffer finalBuffer;

		if (buffers.size() == 0) {
			return "";
		} else if (buffers.size() == 1) {  // fast path for short inputs
			finalBuffer = buffers.get(0);
		} else {
			finalBuffer = ByteBuffer.allocate(total);
			for (ByteBuffer b : buffers) {
				finalBuffer.put(b);
			}
		}
		
		return new String(finalBuffer.array(), 0, finalBuffer.limit(),
						  "ISO-8859-1");
	}

	/**
	 * Read entire contents of an @code{InputStream}, but return only the
	 * contents up to, but not including, the first null (\0) character.
	 *
	 * @param is stream to read from
	 * @return the characters read, not including the null terminator
	 * @throws IOException
	 */
	public static String readUpToNull(InputStream is) throws IOException {
		String contents = readAll(is);
		int indexOfNull = contents.indexOf('\0');
		if (indexOfNull == -1) {
			return contents;
		} else {
			return contents.substring(0, indexOfNull);
		}
	}
}
