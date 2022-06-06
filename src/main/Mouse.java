package main;

import org.lwjgl.glfw.GLFW;

public class Mouse {
	
	private static Mouse instance;
	
	private static double sx;

	private static double sy;
	private static double xPos, yPos, lastX, lastY;
	private static boolean mbp[] = new boolean[3];
	private static boolean isdragging;
	
	private Mouse() {
		
		this.sx = 0.0;
		this.sy = 0.0;
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;
	}
	
//	singelton-obj
	public static Mouse mouse() {
		
		if(Mouse.instance == null) {
			Mouse.instance = new Mouse();
		}
		
		return Mouse.instance;
		
	}
	
	public static void getPosCB(long window, double xpos, double ypos) {
		
		mouse().lastX = mouse().xPos;
		mouse().lastY = mouse().yPos;
		
		mouse().xPos = xpos;
		mouse().yPos = ypos;
		
		mouse().isdragging = mouse().mbp[0] || mouse().mbp[1] || mouse().mbp[2];
	}
	
	public static void mbc(long window, int button, int action, int mod) {
		
		if(action == GLFW.GLFW_PRESS) {
			
			if(button <= mouse().mbp.length) {
				mouse().mbp[button] = true;
			}
		}
		else if(action == GLFW.GLFW_RELEASE) {
			
			if(button <= mouse().mbp.length) {
				mouse().mbp[button] = false;
				mouse().isdragging = false;
			}
		}
		
	}
	
	public static void msc(long window, double xoff, double yoff) {
		
		mouse().sx = xoff;
		mouse().sy = yoff;
		
	}
	
	public static void ef() {
		
		mouse().sx = 0;
		mouse().sy = 0;
		
		mouse().lastX = mouse().xPos;
		mouse().lastY = mouse().yPos;
		
	}
//	evtl. noch alle Variablen static machen
	public static double getSx() {
		return sx;
	}

	public static double getSy() {
		return sy;
	}
//	Deltas aus Video noch einf�gen
	public static double getxPos() {
		return xPos;
	}

	public static double getyPos() {
		return yPos;
	}

	public static boolean[] getMbp() {
//		if (button <= mouse().mbp.length) {
		return mbp;
//		}
	}

	public static boolean isIsdragging() {
		return isdragging;
	}
		
	
	
}
