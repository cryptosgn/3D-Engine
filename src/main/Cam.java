package main;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector2f;

public class Cam {
	
	protected static HashMap<String, Integer> uniforms;
    private Matrix4f projectionMatrix, viewMatrix, transformationMatrix;
    public Vector3f position;
    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;


    public Cam(Vector3f position) {
    	uniforms = new HashMap<>();
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.transformationMatrix = new Matrix4f();
        adjustProjection();
        System.out.println(projectionMatrix);
        System.out.println(viewMatrix);
        System.out.println(transformationMatrix);
    }

    public void adjustProjection() {
        float width = 1920f;
        float height = 1080f;
        float aspectRatio = width / height;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;
        
//        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
        
//        System.out.println(projectionMatrix);
        
    }
    
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                        cameraFront.add(position.x, position.y, 0.0f),
                                        cameraUp);
        
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
    	
        return this.projectionMatrix;  
    }
    
    public Matrix4f getTransformationMatrix() {
    	
    	return this.transformationMatrix;
    }
    
}
