package com.util;

import static org.lwjgl.BufferUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class IOUtil {
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCap) {
		ByteBuffer newBuffer = createByteBuffer(newCap);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public static ByteBuffer resourceToByteBuffer(InputStream source) {
		ByteBuffer buffer = createByteBuffer(1024);
		try {
			ReadableByteChannel rbc = Channels.newChannel(source);
			try {
				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1)
						break;
					if (buffer.remaining() == 0)
						buffer = resizeBuffer(buffer, buffer.limit() << 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					rbc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				source.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		buffer.flip();
		return buffer;
	}
}
