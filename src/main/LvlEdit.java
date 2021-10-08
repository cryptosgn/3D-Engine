package main;

import org.lwjgl.glfw.GLFW;

public class LvlEdit extends Scene {

	private boolean ischangingscenes = false;
	
	private double timelefttochangescenes = 2; 
	
	public LvlEdit(){
		
		System.out.println("test1");
		
		Window.r = 0;
		Window.g = 1;
		Window.b = 0;
		Window.a = 1;
		
	}

	@Override
	public void update(float deltaT) {
		
		if(!ischangingscenes && Keyboard.iskeypressed(GLFW.GLFW_KEY_W)) {
			
			ischangingscenes = true;
			
		}
		
		if(ischangingscenes && timelefttochangescenes > 0) {
			
			timelefttochangescenes -= deltaT;
			Window.r -= deltaT * 5f;
			Window.g -= deltaT * 5f;
			Window.b -= deltaT * 5f;
			Window.a -= deltaT * 5f;
		}
		  
		if(ischangingscenes) {
			Window.changecurrentscene(1);
		}
		
	}
	
}
