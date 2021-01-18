import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.concurrent.ThreadLocalRandom;


public class MouseMove {

    public MouseMove(int destX, int destY, int mouseclicktype){
        try {
            if (mouseclicktype==1) {
                Robot robot = new Robot();
                MouseMotionFactory factory = new MouseMotionFactory();
                factory.getDefault().move(destX, destY);
                Thread.sleep(300);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(150);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                MouseMotionFactory factory = new MouseMotionFactory();
                factory.getDefault().move(destX, destY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
