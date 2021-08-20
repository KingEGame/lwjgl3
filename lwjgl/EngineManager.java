package lwjgl;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;


@Getter
@Setter
public class EngineManager {

    public  static  final  long NANOSECOND = 1000000000L;
    public  static  final  float FRAMERATE = 1000;

    private static  int fps;
    private static  float framerate = 1.0f/FRAMERATE;

    private  boolean isRunnung;

    private  WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void  init() throws  Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        window.init();
        gameLogic.init();
    }

    public  void  start()throws  Exception{
        init();
        if(isRunnung){
            return;
        }
        run();
    }

    public  void run(){
        this.isRunnung =true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunnung){
            boolean render = false;

            long starttime = System.nanoTime();
            long passedtime = starttime - lastTime;
            lastTime = starttime;

            unprocessedTime += passedtime / (double) NANOSECOND;
            frameCounter+= passedtime;

            input();


            while (unprocessedTime > framerate){
                render = true;
                unprocessedTime-= framerate;

                if(window.windowShouldClose()){
                    stop();
                }

                if(frameCounter >= NANOSECOND){
                    setFrps(frames);
                    window.setTitle("Debug" + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private static int getFps() {
        return fps;
    }

    private void setFrps(int fps) {
        EngineManager.fps = fps;
    }

    public void stop(){
        if(!isRunnung){
            return;
        }
    }

    public  void input(){
        gameLogic.input();
    }

    private  void render(){
        gameLogic.render();
        window.update();
    }

    private void update(){
        gameLogic.update();
    }

    private void cleanup(){
        window.cleanuo();
        gameLogic.cleanUp();
        errorCallback.free();
        GLFW.glfwTerminate();
    }
}
