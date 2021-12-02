package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL46;

public class LvlEdit extends Scene {
	
	private String vertexShaderSrc = "layout (location=0) in vec3 aPos;\r\n"
			+ "layout (location=1) in vec4 aColor;\r\n"
			+ "\r\n"
			+ "out vec4 fColor;\r\n"
			+ "\r\n"
			+ "void main(){\r\n"
			+ "fColor = aColor;\r\n"
			+ "gl_Position = vec4(aPos, 1.0);\r\n"
			+ "}";
	
	private String fragmentShaderSrc = "in vec4 fColor;\r\n"
			+ "\r\n"
			+ "out vec4 color;\r\n"
			+ "\r\n"
			+ "void main(){\r\n"
			+ "color = fColor;\r\n"
			+ "}";
	
	private float[] vertexArr = {
//			position			color
//			x,y,z				r,g,b,a
			-1.0f,-1.0f,0.0f,	0.0f,1.0f,0.0f,1.0f, //unten links
			1.0f,-1.0f,0.0f,	1.0f,0.0f,0.0f,1.0f,//unten rechts
			-1.0f,1.0f,0.0f,	0.0f,0.0f,1.0f,1.0f, //oben links
			1.0f,1.0f,0.0f,		0.0f,1.0f,1.0f,1.0f //oben rechts
//			potenzielle Fehlerquelle
	};
	
//	+++Wichtig+++ muss gegen den Uhrzeigersinn verlaufen
	private float[] elementArr = {
			
			3,4,2,
			2,3,4
			
	};
	
	
	
	private int vertexID, fragmentID, ShaderProg;
	
	private int VAOID, VBOID, EBOID;
	
	
	@Override
	public void init() {
//		Shader compilen und linken
		
//		Shader laden und compilen
		
//		eventueller Fehler hier
		vertexID = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
		
		GL46.glShaderSource(vertexID, vertexShaderSrc);
		
		GL46.glCompileShader(vertexID);
		
//		ERRORCHECK
//		i = info
		int funzt = GL46.glGetShaderi(vertexID, GL46.GL_COMPILE_STATUS);
		
		
		
//		eventueller Fehler hier
		fragmentID = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
		
		GL46.glShaderSource(fragmentID, vertexShaderSrc);
		
		GL46.glCompileShader(fragmentID);
		
//		ERRORCHECK
//		i = info
		funzt = GL46.glGetShaderi(fragmentID, GL46.GL_COMPILE_STATUS);
		
		if(funzt == GL46.GL_FALSE) {
//		potenzielle Fehlerquelle
//			int lenght = GL46.glGetShaderi(vertexID, GL46.GL_INFO_LOG_LENGTH);
			
		System.out.println(GL46.glGetShaderInfoLog(fragmentID));
		System.out.println(GL46.glGetShaderInfoLog(vertexID));
			
		assert false: "";
		}
		
		VAOID = GL46.glGenVertexArrays();
		
		GL46.glBindVertexArray(VAOID);
		
		FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(vertexArr.length);
		vertexbuffer.put(vertexArr).flip();
		
		VAOID = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, VAOID);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexbuffer, GL46.GL_STATIC_DRAW);
		
		IntBuffer elementbuffer = BufferUtils.createIntBuffer(elementArr.length);
		vertexbuffer.put(elementArr).flip();
		
		EBOID = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, EBOID);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, elementbuffer, GL46.GL_STATIC_DRAW);
		
//		Arrays Aufteilen
		int positionsize = 4; //x,y,z,w
		int colorsize = 4; //r,g,b,a
		int floatsize = 4; // a float is 4 bytes in java
		int vertexsize = (positionsize + colorsize)*floatsize; // Size of 1 vertex in bytes
		
		GL46.glVertexAttribPointer(0, positionsize, GL46.GL_FLOAT, false, vertexsize, 0);
		GL46.glEnableVertexAttribArray(0);
		
	}
	
	
	public LvlEdit(){
		
		
		
	}

	@Override
	public void update(float deltaT) {
	
	}
}
