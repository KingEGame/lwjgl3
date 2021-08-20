package lwjgl.utils;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;

public class Utils {

    public static FloatBuffer stotDataInFloatBudder(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
}
