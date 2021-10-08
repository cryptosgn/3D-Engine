package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import util.Time;

public class Window {

	int width;
	int height;
	String title;
	long mainwindow = 0;
	
//	getter und setter einbauen
	public static float r = 1;
	public static float g = 1;
	public static float b = 1;
	public static float a = 1;	
	
	protected static Scene currentscene;
	
	protected Window() {
		this.height = 1080;
		this.width = 1920;
		this.title = "GAME";
	}

	public void run() {
		
		init();
		gameloop();

//		not neccasseruy but still a good idea
		GLFW.glfwDestroyWindow(mainwindow);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
		
	}
	
	public static void changecurrentscene(int newcurrent) {
//		Wechsel zwischen LVL-Editor und GAMEPLAY
			switch(newcurrent) {
				
				case 0: {currentscene = new LvlEdit();
		//		currentsceneinit
				break;
				}
				case 1: {currentscene = new LvlScene();
		//		currentsceneinit
				break;
				}	
//				assert schreibt Automatisch Fehlerbericht, wenn es nicht klappt... diese Art ovn Problem hat man vorher händisch mit Kommentaren gemacht
				default: {
					assert false : "Unknown Scene";
					break;
				}
		}
		
	}
	
	private void init() {
		
		System.out.println("System is initiliazing" + "\n" + GLFW.glfwGetVersionString());
		
//		create error-callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Something went wrong... ooops!");
		}
		
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
		
//		creating the window
		mainwindow = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		
		if (mainwindow == 0) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
//		set cursorcallback using delte-expression
		GLFW.glfwSetCursorPosCallback(mainwindow, Mouse::getPosCB);
//		set mousebuttoncallback using delta-expression
		GLFW.glfwSetMouseButtonCallback(mainwindow, Mouse::mbc);
		
		GLFW.glfwSetScrollCallback(mainwindow, Mouse::msc);
		
		GLFW.glfwSetKeyCallback(mainwindow, Keyboard::kcb);
		
		// Make the OpenGL context current -> tells the program which window is meant from there on
		GLFW.glfwMakeContextCurrent(mainwindow);
		
		// Enable v-sync -> Buffer Swapping
		GLFW.glfwSwapInterval(1);

		// Make the window visible
		GLFW.glfwShowWindow(mainwindow);
		
		GL.createCapabilities();
		
		changecurrentscene(0);
	}
	
	private void gameloop() {
		
		float begintime = Time.getTime();
		float deltaT = -1f;
		
		while(!GLFW.glfwWindowShouldClose(mainwindow)) {
		// Poll for window events. The key callback above will only be
		// invoked during this call.
		GLFW.glfwPollEvents();
		
		GL46.glClearColor(r, g, b, a);
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
		
		if(deltaT >= 0) {
			currentscene.update(deltaT);
		}
		
		GLFW.glfwSwapBuffers(mainwindow);
		
		float endtime = Time.getTime();
		deltaT = (endtime - begintime);
		begintime = endtime;
		
//		System.out.println(deltaT);
		}
	}
	
}
