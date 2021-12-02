package main;

import org.lwjgl.glfw.GLFW;

public class LvlScene extends Scene {
	
	private boolean ischangingscenes = false;
	
	private double timelefttochangescenes = 2; 
	
	public LvlScene() {
		
		System.out.println("test2");
		
		Window.r = 1;
		Window.g = 1;
		Window.b = 0;
		Window.a = 1;
		
	}

	@Override
	public void update(float deltaT) {
		
		if(!ischangingscenes && Keyboard.iskeypressed(GLFW.GLFW_KEY_SPACE)) {
			
			ischangingscenes = true;
			
		}
		
		if(ischangingscenes && timelefttochangescenes > 0) {
			
			timelefttochangescenes -= deltaT;
			Window.r -= deltaT * 8f;
			Window.g -= deltaT * 5f;
			Window.b -= deltaT * 2f;
			Window.a -= deltaT * 5f;
			
		}
//		oben stehende Bedingung muss erst eintrten
//			wenn keine Zeit mehr aber Szenen noch gewechselt werden m�ssen (n�chster update-Call)
		else if(ischangingscenes) { 
			Window.changecurrentscene(0);
			ischangingscenes = false;
		}
		
	}
	
}
