package lwjgl;

import lwjgl.entity.Model;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Game implements ILogic{

    private  int direction = 0;
    private  float colour = 0.0f;

    private final  RenderManager render;
    private final ObjectsLoader loader;
    private  final WindowManager window;
    private Model model;

    public Game(){
        render = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectsLoader();
    }

    @Override
    public void init() throws Exception {
        render.init();

        float[] verteces = {
                -0.5f,0.5f,0f,
                -0.5f,-0.5f,0f,
                0.5f,-0.5f,0f,
                0.5f,-0.5f,0f,
                0.5f,0.5f,0f,
                -0.5f,0.5f,0f
        };

        model = loader.loadmodel(verteces);

    }

    @Override
    public void input() {
        if(window.isKeyPressed(GLFW_KEY_UP)){
            direction = 1;
        }else if(window. isKeyPressed(GLFW_KEY_DOWN)){
            direction = -1;
        }else{
            direction = 0;
        }
    }

    @Override
    public void update() {
        colour += direction * 0.01f;
        if(colour > 1){
            colour = 1.0f;
        }else if(colour <= 0){
            colour = 0.0f;
        }
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColour(colour,colour,colour,0.0f);
        render.render(model);
//        render.clear();
    }

    @Override
    public void cleanUp() {
        render.cleanUp();
        loader.cleanup();
    }
}
