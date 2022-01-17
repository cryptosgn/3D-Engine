package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.openvr.Texture;
import org.lwjgl.system.MemoryStack;

import util.Textures;

public class LvlEdit extends Scene {
	
	private String vertexShaderSrc = "#version 460\r\n"
			+ "layout (location=0) in vec3 aPos;\r\n"
			+ "layout (location=1) in vec4 aColor;\r\n"
			+ "layout (location=2) in vec2 aTexCoords;\r\n"
			+ "\r\n"
			+ "uniform mat4 uProj;\r\n"
			+ "uniform mat4 uView;\r\n"
			+ "\r\n"
			+ "out vec4 fColor;\r\n"
			+ "out vec2 fTexCoords;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    fColor = aColor;\r\n"
			+ "    fTexCoords = aTexCoords;\r\n"
			+ "    gl_Position = uProj * uView * vec4(aPos, 1.0);\r\n"
			+ "}";
	
	private String fragmentShaderSrc = "#version 460\r\n"
			+ "\r\n"
			+ "uniform float uTime;\r\n"
			+ "uniform sampler2D TEX_SAMPLER;\r\n"
			+ "\r\n"
			+ "in vec4 fColor;\r\n"
			+ "in vec2 fTexCoords;\r\n"
			+ "\r\n"
			+ "out vec4 color;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    color = texture(TEX_SAMPLER, fTexCoords);\r\n"
			+ "}";
	
	private float[] vertexArr = {
//			position			color
//			x,y,z				r,g,b,a
		    -0.5f,  0.5f,  0.5f,	0.5f, 0.0f, 0.0f, 1.0f,
		    -0.5f, -0.5f,  0.5f,	0.0f, 0.5f, 0.0f, 1.0f,
		    0.5f, -0.5f,  0.5f,		0.0f, 0.0f, 0.5f, 1.0f,
		    0.0f, 0.0f, 0.5f,		0.0f, 0.5f, 0.5f, 1.0f,
		    -0.5f,  0.5f, -0.5f,	0.5f, 0.0f, 0.0f, 1.0f,
		    0.5f,  0.5f, -0.5f,		0.0f, 0.5f, 0.0f, 1.0f,
		    -0.5f, -0.5f, -0.5f,	0.0f, 0.0f, 0.5f, 1.0f,
		    0.5f, -0.5f, -0.5f,	    0.0f, 0.5f, 0.5f, 1.0f
	};
	
//	+++Wichtig+++ muss gegen den Uhrzeigersinn verlaufen
	private int[] elementArr = {
			
		    // Front face
		    0, 1, 3, 3, 1, 2,
		    // Top Face
		    4, 0, 3, 5, 4, 3,
		    // Right face
		    3, 2, 7, 5, 3, 7,
		    // Left face
		    6, 1, 0, 6, 0, 4,
		    // Bottom face
		    2, 1, 6, 2, 6, 7,
		    // Back face
		    7, 6, 4, 7, 4, 5
			
	};
	
	
	
	private int vertexID, fragmentID, ShaderProg;
	
	private int VAOID, VBOID, EBOID;
	
	private Textures testTexture;
	
	@Override
	public void init() {
		
		this.ZweiundVierzig = new Cam(new Vector2f());
//		Shader compilen und linken	
//		Shader laden und compilen
		vertexID = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
		
		GL46.glShaderSource(vertexID, vertexShaderSrc);
		
		GL46.glCompileShader(vertexID);
		
		int funzt = GL46.glGetShaderi(vertexID, GL46.GL_COMPILE_STATUS);
		
        int success = GL46.glGetShaderi(vertexID, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(vertexID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + "'\n\tVertex shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
		
		fragmentID = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
		
		GL46.glShaderSource(fragmentID, fragmentShaderSrc);
		
		GL46.glCompileShader(fragmentID);
		
        success = GL46.glGetShaderi(fragmentID, GL46.GL_COMPILE_STATUS);
        if (success == GL46.GL_FALSE) {
            int len = GL46.glGetShaderi(fragmentID, GL46.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + "'\n\tFragment shader compilation failed.");
            System.out.println(GL46.glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        
		if(funzt == GL46.GL_FALSE) {
			
		System.out.println(GL46.glGetShaderInfoLog(fragmentID));
		System.out.println(GL46.glGetShaderInfoLog(vertexID));
			
		assert false: "";
		}
		
		this.testTexture = new Textures("Textures/testImage.png");
		
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
		
		VBOID = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, VBOID);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexbuffer, GL46.GL_STATIC_DRAW);
		
		IntBuffer elementbuffer = BufferUtils.createIntBuffer(elementArr.length);
		elementbuffer.put(elementArr).flip();
		
		EBOID = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, EBOID);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, elementbuffer, GL46.GL_STATIC_DRAW);
		
//		Arrays Aufteilen
		int positionsize = 3; //x,y,z
		int colorsize = 4; //r,g,b,a
		int floatsize = 4; // a float is 4 bytes in java
		int vertexsize = (positionsize + colorsize)*floatsize; // Size of 1 vertex in bytes
		
//		Position 1 ist von der Grï¿½ï¿½e positionsize
		GL46.glVertexAttribPointer(0, positionsize, GL46.GL_FLOAT, false, vertexsize, 0);
		GL46.glEnableVertexAttribArray(0);
//		Der Letzte Wert dieser Methode ist der Pointer, was beduted, dass in diesem Falle das Ganze Auf diese Position "zeigt", worï¿½ber es dann abgerufen werden kann
		GL46.glVertexAttribPointer(1, colorsize, GL46.GL_FLOAT, false, vertexsize, positionsize*floatsize);
		GL46.glEnableVertexAttribArray(1);
	}
	
	
	public LvlEdit(){
		
		System.out.println("Welcome to the Lvl-Editor");
	}

	@Override
	public void update(float deltaT) {
		
		// Update rotation angle
//		float rotation = gameItem.getRotation().x + 1.5f;
//		if ( rotation > 360 ) {
//		    rotation = 0;
//		}
//		gameItem.setRotation(rotation, rotation, rotation);
		
//        ZweiundVierzig.position.x -= deltaT * 50.0f;
//        ZweiundVierzig.position.y -= deltaT * 20.0f;

		 // Bind shader program
        GL46.glUseProgram(ShaderProg);
        // Bind the VAO that we're using
        GL46.glBindVertexArray(VAOID);

        // Enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        
//        kann in späteren Versionen ausgelagert werden
		try {
			createUniform("uProj");
			createUniform("uView");
//			createUniform("uTime");
			createUniform("TEX_SAMPLER");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        setUniform("uProj", ZweiundVierzig.getProjectionMatrix());
        setUniform("uView", ZweiundVierzig.getViewMatrix());
//        setUniformfloat("uView", 1.0f);  
        
        uploadTexture("TEX_SAMPLER", 0);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        testTexture.bind();
        
//        Wireframe-mode
        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_LINE);
        
        GL46.glDrawElements(GL46.GL_TRIANGLES, elementArr.length, GL46.GL_UNSIGNED_INT, 0);
        
//        disable Wireframe
        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_FILL);
        // Unbind everything

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        GL46.glBindVertexArray(0);

        GL46.glUseProgram(0);
		
	}
	
    	public void createUniform(String uniformName) throws Exception {
    	    int uniformLocation = GL46.glGetUniformLocation(ShaderProg,
    	        uniformName);
    	    if (uniformLocation == -1) {
    	        throw new Exception("Could not find uniform:" +
    	            uniformName);
    	    }
    	    Cam.uniforms.put(uniformName, uniformLocation);
    }
    	
    	public void setUniform(String uniformName, Matrix4f value) {
    	    // Dump the matrix into a float buffer
    	    try (MemoryStack stack = MemoryStack.stackPush()) {
    	        FloatBuffer fb = stack.mallocFloat(16);
    	        value.get(fb);
//    	        System.out.println(Cam.uniforms.get(uniformName));
    	        GL46.glUniformMatrix4fv(Cam.uniforms.get(uniformName), false, fb);
    	    }
    	}
    	
    	public void setUniformfloat(String uniformName, float value) {
    	    // Dump the matrix into a float buffer
    	    try (MemoryStack stack = MemoryStack.stackPush()) {
    	        FloatBuffer fb = stack.mallocFloat(1);
//    	        System.out.println(Cam.uniforms.get(uniformName));
    	        GL46.glUniformMatrix4fv(Cam.uniforms.get(uniformName), false, fb);
    	    }
    	}
    	
        public void uploadTexture(String varName, int slot) {
            int varLocation = GL46.glGetUniformLocation(ShaderProg, varName);
            GL46.glUniform1i(varLocation, slot);
        }
	
}
