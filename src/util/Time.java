package util;

import org.lwjgl.glfw.GLFW;

public class Time {

	public static float inittime = System.nanoTime();
	
	public static float getTime() {
//		1E-9 -> nanosecs to secs
		return (float) ((System.nanoTime() - inittime)* 1E-9);
		
	}
	
}
