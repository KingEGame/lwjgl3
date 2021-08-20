package lwjgl;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Launcher {

    private static WindowManager window;

    public static Game getGame() {
        return game;
    }

    //    private static EngineManager engine;
    private static Game game;

    public static WindowManager getWindow() {
        return window;
    }

    public static void main(String[] args) {
        window = new WindowManager("debug", 1600,900,false);
        game = new Game();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
