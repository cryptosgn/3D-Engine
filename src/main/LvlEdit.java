package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

public class LvlEdit extends Scene {
	
	private String vertexShaderSrc = "#version 460\r\n"
			+ "layout (location=0) in vec3 aPos;\r\n"
			+ "layout (location=1) in vec4 aColor;\r\n"
			+ "\r\n"
			+ "out vec4 fColor;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    fColor = aColor;\r\n"
			+ "    gl_Position = vec4(aPos, 1.0f);\r\n"
			+ "}";
	
	private String fragmentShaderSrc = "#version 460\r\n"
			+ "\r\n"
			+ "in vec4 fColor;\r\n"
			+ "\r\n"
			+ "out vec4 color;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    color = fColor;\r\n"
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
		
		
		fragmentID = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
		
		GL46.glShaderSource(vertexID, vertexShaderSrc);
		
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
		
//		Shader linken
		ShaderProg = GL46.glCreateProgram();
		GL46.glAttachShader(ShaderProg, vertexID);
		GL46.glAttachShader(ShaderProg, fragmentID);
		GL46.glLinkProgram(ShaderProg);
		
		funzt = GL46.glGetProgrami(ShaderProg, GL46.GL_LINK_STATUS);
		
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
		int positionsize = 3; //x,y,z
		int colorsize = 4; //r,g,b,a
		int floatsize = 4; // a float is 4 bytes in java
		int vertexsize = (positionsize + colorsize)*floatsize; // Size of 1 vertex in bytes
		
//		Position 1 ist von der Größe positionsize
		GL46.glVertexAttribPointer(0, positionsize, GL46.GL_FLOAT, false, vertexsize, 0);
		GL46.glEnableVertexAttribArray(0);
//		Der Letzte Wert dieser Methode ist der Pointer, was beduted, dass in diesem Falle das Ganze Auf diese Position "zeigt", worüber es dann abgerufen werden kann
		GL46.glVertexAttribPointer(1, colorsize, GL46.GL_FLOAT, false, vertexsize, positionsize*floatsize);
		GL46.glEnableVertexAttribArray(1);
	}
	
	
	public LvlEdit(){
		
		init();
		
	}

	@Override
	public void update(float deltaT) {
	
		 // Bind shader program
        GL46.glUseProgram(ShaderProg);
        // Bind the VAO that we're using
        GL46.glBindVertexArray(VAOID);

        // Enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glDrawElements(GL46.GL_TRIANGLES, elementArr.length, GL46.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        GL46.glBindVertexArray(0);

        GL46.glUseProgram(0);
		
	}
}
