package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import org.lwjgl.openvr.Texture;
import org.lwjgl.system.MemoryStack;

import util.Textures;

public class LvlEdit extends Scene {
	
	private String vertexShaderSrc = "#version 460\r\n"
			+ "layout (location=0) in vec3 aPos;\r\n"
			+ "layout (location=1) in vec4 aColor;\r\n"
			+ "\r\n"
			+ "uniform mat4 uProj;\r\n"
			+ "uniform mat4 uTrans;\r\n"
			+ "uniform mat4 uView;\r\n"
			//+ "uniform mat4 transform;\r\n"
			+ "\r\n"
			+ "out vec4 fColor;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
			+ "    fColor = aColor;\r\n"
			+ "    gl_Position = uProj * uView * uTrans * vec4(aPos, 1.0);\r\n"
			+ "}";
	
	private String fragmentShaderSrc = "#version 460\r\n"
			+ "\r\n"
			+ "uniform float uTime;\r\n"
			+ "\r\n"
			+ "in vec4 fColor;\r\n"
			+ "\r\n"
			+ "out vec4 color;\r\n"
			+ "\r\n"
			+ "void main()\r\n"
			+ "{\r\n"
//			+ "    float noise = fract(sin(dot(fColor.xy ,vec2(12.9898,78.233))) * 43758.5453);\r\n"
			+ "    color = fColor;\r\n"
			+ "}";
	
	private float[] vertexArr = {
//			position			color
//			x,y,z				r,g,b,a
		    -0.5f,  0.5f,  0.5f,	1.0f, 0.0f, 0.0f, 1.0f,
		    -0.5f, -0.5f,  0.5f,	0.0f, 1.0f, 0.0f, 1.0f,
		    0.5f, -0.5f,  0.5f,		0.0f, 0.0f, 1.0f, 1.0f,
		    0.0f, 0.0f, 0.5f,		0.0f, 1.0f, 1.0f, 1.0f,
		    -0.5f,  0.5f, -0.5f,	1.0f, 0.0f, 0.0f, 1.0f,
		    0.5f,  0.5f, -0.5f,		0.0f, 0.5f, 0.0f, 1.0f,
		    -0.5f, -0.5f, -0.5f,	0.0f, 0.0f, 1.0f, 1.0f,
		    0.5f, -0.5f, -0.5f,	    0.0f, 1.0f, 1.0f, 1.0f
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
		
		this.ZweiundVierzig = new Kamera(0f,0f,1f);
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
		
//		Position 1 ist von der Gr��e positionsize
		GL46.glVertexAttribPointer(0, positionsize, GL46.GL_FLOAT, false, vertexsize, 0);
		GL46.glEnableVertexAttribArray(0);
//		Der Letzte Wert dieser Methode ist der Pointer, was beduted, dass in diesem Falle das Ganze Auf diese Position "zeigt", wor�ber es dann abgerufen werden kann
		GL46.glVertexAttribPointer(1, colorsize, GL46.GL_FLOAT, false, vertexsize, positionsize*floatsize);
		GL46.glEnableVertexAttribArray(1);
	}
	
	
	public LvlEdit(){
		
		System.out.println("Welcome to the Lvl-Editor");
	}
	
	private double mouseposxdelta, mouseposydelta = 0d;
	
	private float rotationyaw = 0;
	private float rotationpitch = 0;
	
	@Override
	public void update(float deltaT) {
		
//		movement (Problem: Wird sich immer den Achsen entsprechen bewegen -> Mousemovement irrelevant geworden)
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_S)) {
			ZweiundVierzig.changeposition(0f, 0f, 0.01f);
		}
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_W)) {
			ZweiundVierzig.changeposition(0f, 0f, -0.01f);
		}
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_A)) {
			ZweiundVierzig.changeposition(-0.01f, 0f, 0f);
		}
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_D)) {
			ZweiundVierzig.changeposition(0.01f, 0f, 0f);
		}
		
//		rotation angle
//		keyboardtest (noch durch mouse austauschen)
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_Q)) {
			ZweiundVierzig.mouseinput(this.rotationyaw++, this.rotationpitch);
		}
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_E)) {
			ZweiundVierzig.mouseinput(this.rotationyaw--, this.rotationpitch);
		}	
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_X)) {
			ZweiundVierzig.mouseinput(this.rotationyaw, this.rotationpitch++);
		}
		if(Keyboard.iskeypressed(GLFW.GLFW_KEY_C)) {
			ZweiundVierzig.mouseinput(this.rotationyaw, this.rotationpitch--);
		}	
		
		
//		  Bind shader program
        GL46.glUseProgram(ShaderProg);
//         Bind the VAO that we're using
        GL46.glBindVertexArray(VAOID);

//         Enable the vertex attribute pointers
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);
        
//        kann in sp�teren Versionen ausgelagert werden
		try {
			createUniform("uProj");
			createUniform("uView");
			createUniform("uTrans");
//			createUniform("uTime");
//			createUniform("TEX_SAMPLER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        setUniform("uProj", ZweiundVierzig.getProjectionMatrix());
        setUniform("uView", ZweiundVierzig.getViewMatrix());
        setUniform("uTrans", ZweiundVierzig.getTransformationMatrix());
//        setUniformfloat("uView", 1.0f);  
        
        uploadTexture("TEX_SAMPLER", 0);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        testTexture.bind();
        
//        Wireframe-mode
//        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_LINE); 
        
        GL46.glDrawElements(GL46.GL_TRIANGLES, elementArr.length, GL46.GL_UNSIGNED_INT, 0);
        
//        disable Wireframe
//        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_FILL);
        
//        Unbind everything
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
    	    Kamera.uniforms.put(uniformName, uniformLocation);
    }
    	
    	public void setUniform(String uniformName, Matrix4f value) {
    	    // Dump the matrix into a float buffer
    	    try (MemoryStack stack = MemoryStack.stackPush()) {
    	        FloatBuffer fb = stack.mallocFloat(16);
    	        value.get(fb);
//    	        System.out.println(Kamera.uniforms.get(uniformName));
    	        GL46.glUniformMatrix4fv(Kamera.uniforms.get(uniformName), false, fb);
    	    }
    	}
    	
    	public void setUniformfloat(String uniformName, float value) {
    	    // Dump the matrix into a float buffer
    	    try (MemoryStack stack = MemoryStack.stackPush()) {
    	        FloatBuffer fb = stack.mallocFloat(1);
//    	        System.out.println(Kamera.uniforms.get(uniformName));
    	        GL46.glUniformMatrix4fv(Kamera.uniforms.get(uniformName), false, fb);
    	    }
    	}
    	
        public void uploadTexture(String varName, int slot) {
            int varLocation = GL46.glGetUniformLocation(ShaderProg, varName);
            GL46.glUniform1i(varLocation, slot);
        }
	
}
