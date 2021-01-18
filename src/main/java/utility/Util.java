package utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Brandon on 2015-12-20.
 */
public class Util {
    private static final Random random = new Random();
    private static Util instance = new Util();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-hh-mm-ss");

    private Util() {
    }

    public static Util getInstance() {
        return instance;
    }

    public static int random(int Min, int Max) {
        return random.nextInt(Math.abs(Max - Min)) + Min;
    }

    public static void sleep(int Time) {
        try {
            Thread.sleep(Time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepRandom(int Min, int Max) {
        sleep(random(Min, Max));
    }

    private static Date getDate() {
        return Calendar.getInstance().getTime();
    }

    public static Image loadResourceImage(String ResourcePath) {
        try {
            return ImageIO.read(instance.getClass().getResource(ResourcePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageIcon loadIcon(String url) {
        try {
            return new ImageIcon(new URL(url));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
