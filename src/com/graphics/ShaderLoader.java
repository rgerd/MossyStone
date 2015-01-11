package com.graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import com.util.IOUtil;

public class ShaderLoader {
	public static int loadProgram(String name) {
		int program = glCreateProgram();
		int v_s = loadShader(name, GL_VERTEX_SHADER);
		int f_s = loadShader(name, GL_FRAGMENT_SHADER);

		glAttachShader(program, v_s);
		glAttachShader(program, f_s);

		glBindAttribLocation(program, 0, "vertex");

		glBindFragDataLocation(program, 0, "color");
		
		glLinkProgram(program);
		int linked = glGetProgrami(program, GL_LINK_STATUS);
		String programLog = glGetProgramInfoLog(program);
		
		if (!programLog.trim().isEmpty()) {
			System.err.println(programLog);
		}
		if (linked == 0) {
			throw new AssertionError("Could not link program");
		}
		
		return program;
	}

	private static int loadShader(String name, int type) {
		int object = glCreateShader(type);
		ByteBuffer buffer = IOUtil.resourceToByteBuffer(ShaderLoader.class.getResourceAsStream("/glsl/" + name + ".vs"));
		PointerBuffer strings = BufferUtils.createPointerBuffer(1);
		IntBuffer lengths = BufferUtils.createIntBuffer(1);
		strings.put(0, buffer);
		lengths.put(0, buffer.remaining());

		glShaderSource(object, strings, lengths);
		glCompileShader(object);
		int compiled = glGetShaderi(object, GL_COMPILE_STATUS);
		String shaderLog = glGetShaderInfoLog(object);
		if (!shaderLog.trim().isEmpty()) {
			System.err.println(shaderLog);
		}

		if (compiled == 0) {
			throw new AssertionError("Could not compile shader");
		}

		return object;
	}
}
