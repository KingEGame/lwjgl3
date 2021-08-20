package lwjgl;

import lombok.*;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

@Getter
@Setter
public class WindowManager {

    public static  final float FOV = (float) Math.toRadians(60);
    public static  final float Z_NEAR = 0.01f;
    public static  final float Z_FAR = 1000f;

    private final String title;

    private int width, height;
    private long window;

    private final Matrix4f projectionMatrix;

    private boolean resize, vSync;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.projectionMatrix = new Matrix4f();
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initalize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        org.lwjgl.glfw.GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT,GLFW_TRUE);

        boolean maximixed = false;

        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximixed = true;
        }

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(window == NULL){
            throw  new RuntimeException("Failed to create GLFW Window");
        }

        glfwSetFramebufferSizeCallback(window, (window, width, height)-> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
           if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
               glfwSetWindowShouldClose(window, true);
           }
        });

        if(maximixed){
            glfwMaximizeWindow(window);
        }else {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidMode.width() - width)/2, (vidMode.height() - height) / 2);
        }

        glfwMakeContextCurrent(window);

        if(isVSync()){
            glfwSwapInterval(1);
        }

        glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

    }

    public  void update(){
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public  void cleanuo(){
        glfwDestroyWindow(window);
    }

    public void setClearColour(float r, float g, float b, float a){
        GL11.glClearColor(r,g,b,a);
    }

    public boolean isKeyPressed(int keycode){
        return  glfwGetKey(window, keycode) == GLFW_PRESS;
    }

    public  boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    public void  setTitle(String title){
        glfwSetWindowTitle(window, title);
    }

    public String getTitle(){
        return title;
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width/height;
        return  projectionMatrix.setPerspective(FOV,aspectRatio, Z_NEAR, Z_FAR);
    }

    public  Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio = (float) width/height;
        return  projectionMatrix.setPerspective(FOV,aspectRatio, Z_NEAR, Z_FAR);
    }






}
