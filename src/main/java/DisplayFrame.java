import utility.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DisplayFrame {

    private static JLabel splash = new JLabel(Util.loadIcon("http://www.runescape.com/img/game/splash.gif"));

    public DisplayFrame(String world, int width, int height) {
        JFrame frame = new JFrame("UNIBOT - Alpha 0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getX();
        if (splash != null) {
            frame.add(splash);
            frame.pack();
            frame.setVisible(true);
        }

        centerFrame(frame);

        ClientApplet applet = new ClientApplet(world, "", width, height);
        frame.add(applet);
        frame.setSize(width, height);
        applet.start();
        if (splash != null) {
            frame.remove(splash);
        }
        frame.revalidate();
        frame.pack();

        centerFrame(frame);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                frame.dispose();
                super.windowClosed(e);
            }
        });



    }

    private static void centerFrame(JFrame frame) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int cX = (toolkit.getScreenSize().width / 2) - (frame.getWidth() / 2);
        int cY = (toolkit.getScreenSize().height / 2) - (frame.getHeight() / 2);
        frame.setMinimumSize(frame.getSize());
        frame.setLocation(cX, cY);
        frame.setVisible(true);
        frame.setResizable(false);
    }

}