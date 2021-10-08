package main;

import org.lwjgl.glfw.GLFW;

public class Keyboard {

	private static Keyboard instance;
//	LWJGL has exactly 350 possible keybindings
	private boolean[] keypressed = new boolean[350];
	
	public static Keyboard keyboard() {
		
		if(Keyboard.instance == null) {
			Keyboard.instance = new Keyboard();
		}
		
		return Keyboard.instance;
		
	}
	
	public static void kcb(long window, int key, int scancode, int action, int mods) {
		
		if(action == GLFW.GLFW_PRESS) {
			keyboard().keypressed[key] = true;
		}
		else if(action == GLFW.GLFW_RELEASE) {
			keyboard().keypressed[key] = false;
		}
		
	}
	
	public static boolean iskeypressed(int keycode) {
//		check: in-bounds
		if(keycode <= keyboard().keypressed.length) {
			return keyboard().keypressed[keycode];
		}
		else {		
		return false;
		
		}
	}

}
