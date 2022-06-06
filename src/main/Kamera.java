package main;

import java.util.HashMap;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Kamera {
	
	private Vector3f position, lookDir, orientation;
	private float yaw, pitch;
	
	private Matrix4f projectionMatrix, transformationMatrix;
	
	protected static HashMap<String, Integer> uniforms;
	
	public Kamera(float x, float y, float z) {
		this.position = new Vector3f(x,y,z);
		this.lookDir = new Vector3f(0,0,-1); //-1 -> forward
		this.orientation = new Vector3f(0,1,0); // 1 -> geradeaus|nicht nach unten
		
		this.projectionMatrix = new Matrix4f();
		this.transformationMatrix = new Matrix4f();
		
		uniforms = new HashMap<>();		
	}
	
	public void changeposition(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
		
	}
	
	public void mouseinput(float x, float y) {
		yaw = x;
		pitch = y;
		
	}
	
	public Matrix4f getViewMatrix() {
		Vector3f lookPoint = new Vector3f(0,0,-1);
		
		lookPoint.rotateY(Math.toRadians(yaw), lookPoint);
		lookPoint.rotateX(Math.toRadians(pitch), lookPoint);
		
//		lookPoint.add(position, lookPoint);
		lookPoint.add(position);
		
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.lookAt(this.position, lookPoint, this.orientation);
		
		return viewMatrix;
	}
	
    public Matrix4f getProjectionMatrix() {
    	
        return this.projectionMatrix;  
    }
    
    public Matrix4f getTransformationMatrix() {
    	
    	return this.transformationMatrix;
    }
	
}
