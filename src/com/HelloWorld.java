package com;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.graphics.ShaderLoader;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
 
public class HelloWorld {
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
 
    private long window;
    
    private int lol;
 
    public void run() {
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
 
        try {
            init();
            loop();
 
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }
 
    private void init() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");
 
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
 
        int WIDTH = 800;
        int HEIGHT = 640;
        
        window = glfwCreateWindow(WIDTH, HEIGHT, "Mossy Stone Pre-Alpha", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE);
            }
        });
 
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (GLFWvidmode.width(vidmode) - WIDTH) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );
 
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);
        

        GLContext.createFromCurrent();
        
        //ShaderLoader.loadProgram("simple");
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //glOrtho(0, 800.0, 640.0, 0, 0.0, 1.0);
        
        gluOrtho2D(0, 800.0, 640.0, 0);
        //glMatrixMode(GL_MODELVIEW);
        
        lol = glGenLists(1);
        glNewList(lol, GL_COMPILE);
        
        glColor3f(1.0f, 0.0f, 1.0f);
        glBegin(GL_QUADS);
        glVertex3f(0.0f, 0.0f, 0.5f);
        glVertex3f(0.0f, 100.0f, 0.5f);
        glVertex3f(100.0f, 0.0f, 0.5f);
        glVertex3f(100.0f, 100.0f, 0.5f);
        glEnd();
        
        glEndList();
        
        System.out.println(lol);
        
        frames = 0;
        lastTime = System.currentTimeMillis();
    }
 
    private long lastTime;
    private int frames;
    
    private void loop() {
        //GLContext.createFromCurrent();
 
    	glClearColor((float)(Math.random() * 0.5), (float)(Math.random() * 0.5), (float)(Math.random() * 0.5), 1.f);
    	
        while (glfwWindowShouldClose(window) == GL_FALSE) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            
            glPushMatrix();
            glCallList(lol);
            glPopMatrix();
            
            glfwPollEvents();
            
            frames++;
            
            if(System.currentTimeMillis() - lastTime > 1000) {
            	lastTime += 1000;
            	System.out.println(frames);
            	frames = 0;
            }
        }
    }
 
    public static void main(String[] args) {
        new HelloWorld().run();
    }
 
}