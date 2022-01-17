package main;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector2f;

public class Cam {
	
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;
    private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float aspectRatio = 1920.0f / 1080.0f;

    public Cam(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
    	projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
    	0.01f, 1000.0f);
        
    }
    
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                        cameraFront.add(position.x, position.y, 0.0f),
                                        cameraUp);
//        In OGL eine Scale-Methode, sowie eine Pitch- und Yaw-Methode finden

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
    
}
