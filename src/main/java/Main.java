import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;
import org.opencv.utils.Converters;
import utility.Util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;



public class Main {

    public static boolean keyBindPressed = false;
    public static int finalXY = 0;
    public static int scriptStep = 0;

    public static String selectedScript = "";

    public static int worldSelect = 48;

    private static JLabel splash = new JLabel(Util.loadIcon("http://www.runescape.com/img/game/splash.gif"));

    public static JFrame frame = new JFrame("UNIBOT IDE - Alpha 1.0.3");

    public static int negSC = 0;

//    public static String currentDirectory = new File("").getAbsolutePath();

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        //OSRS Launcher
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        JMenuBar m = new JMenuBar();

//        menu.add(new JSeparator());

        JMenu m1 = new JMenu("File");
        JMenu mA1 = new JMenu("New");
        JMenuItem miA1 = new JMenuItem("Basic Script...");
        JMenuItem miA2 = new JMenuItem("Dynamic Script...");
        JMenuItem mA2 = new JMenuItem("Open...");
        JMenuItem mA3 = new JMenuItem("Save All");
        mA3.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        JMenu mA4 = new JMenu("IDE Settings");
        JMenuItem mA5 = new JMenuItem("Configure");
        mA5.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

        JMenu m2 = new JMenu("Edit");
        JMenuItem mB1 = new JMenuItem("Undo");
        mB1.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        JMenuItem mB2 = new JMenuItem("Redo");
        mB2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
        JMenuItem mB3 = new JMenuItem("Cut");
        mB3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        JMenuItem mB4 = new JMenuItem("Copy");
        mB4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        JMenuItem mB5 = new JMenuItem("Paste");
        mB5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        JMenuItem mB6 = new JMenuItem("Delete");

        JMenuItem mB7 = new JMenuItem("Find");

        JMenuItem mB8 = new JMenuItem("Select All");

        JMenu m3 = new JMenu("View");

        JMenu m4 = new JMenu("Build");

        JMenu m5 = new JMenu("No Script Selected");

        JMenu m6 = new JMenu("Tools");
        JMenuItem mF1 = new JMenuItem("Send to Origin");
        JMenuItem mF2 = new JMenuItem("Get Current Mouse Coordinates");
        mF2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));

        JMenu m7 = new JMenu("Guide");

        //Under File
        m1.add(mA1);
        m1.add(mA2);
        m1.add(new JSeparator());
        m1.add(mA3);
        m1.add(new JSeparator());
        m1.add(mA4);
        m1.add(mA5);
        //Under Edit
        m2.add(mB1);
        m2.add(mB2);
        m2.add(new JSeparator());
        m2.add(mB3);
        m2.add(mB4);
        m2.add(mB5);
        m2.add(mB6);
        m2.add(new JSeparator());
        m2.add(mB7);
        m2.add(mB8);
        //Under Tools
        m6.add(mF1);
        m6.add(new JSeparator());
        m6.add(mF2);

        mA1.add(miA1);
        mA1.add(miA2);

        m.add(m1);
        m.add(m2);
        m.add(m3);
        m.add(m4);
        m.add(m5);
        m.add(m6);
        m.add(m7);

        mF1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setLocation(0,0);
                System.out.println("Sent to Origin...");
            }
        });

        mF2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_F10);
                    r.keyRelease(KeyEvent.VK_F10);

                } catch (AWTException awtException) {
                    awtException.printStackTrace();
                }
            }
        });


        frame.setJMenuBar(m);

        if (splash != null) {
            frame.add(splash);
            frame.pack();
            frame.setVisible(true);
        }

        centerFrame(frame);
        JButton intFrame =  new JButton();
        JTextArea textArea = new JTextArea();
        JTextArea textArea2 = new JTextArea();
        ClientApplet applet = new ClientApplet("http://oldschool" + worldSelect + ".runescape.com", "",800, 600);
        applet.setBounds(0, 0, 800, 600);
        frame.add(applet);
        frame.setSize(800, 600);
        applet.start();

        if (splash != null) {
            frame.remove(splash);
        }

        frame.revalidate();
        frame.pack();
        frame.setSize(1000, 800);

        Color codeGrey = new Color(49,49, 49, 255);
        Color codeFont = new Color(174,190, 203, 255);

        textArea2.setMargin(new Insets(5, 10, 5, 10));
        textArea2.setCaretColor(Color.WHITE);
        textArea2.setBounds(804, 0, 196, 800);
        textArea2.setColumns(100);
        textArea2.setRows(100);

        textArea2.setFont(new Font("Arial", Font.PLAIN, 13));

        textArea2.setForeground(codeFont);
        textArea2.setBackground(codeGrey);
        frame.add(textArea2);
        intFrame.setBounds(0, 600, 800, 180);
        frame.add(intFrame);
        textArea.setBounds(0, 0, 0, 0);
        frame.add(textArea);
        centerFrame(frame);

        mA2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String currentDirectory = new File("").getAbsolutePath();
                // Create an object of JFileChooser class
                JFileChooser j = new JFileChooser(currentDirectory + "/scripts/");

                // Invoke the showsOpenDialog function to show the save dialog
                int r = j.showOpenDialog(null);

                // If the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    // Set the label to the path of the selected directory
                    File fi = new File(j.getSelectedFile().getAbsolutePath());

                    try {
                        // String
                        String s1 = "", sl = "";

                        // File reader
                        FileReader fr = new FileReader(fi);

                        // Buffered reader
                        BufferedReader br = new BufferedReader(fr);

                        // Initilize sl
                        sl = br.readLine();

                        // Take the input from the file
                        while ((s1 = br.readLine()) != null) {
                            sl = sl + "\n" + s1;
                        }

                        textArea2.setText(sl);
                        selectedScript = fi.getName();
                        m5.setText(selectedScript);
                        frame.setTitle("UNIBOT IDE - " + selectedScript);

                        System.out.println(selectedScript);


                    }
                    catch (Exception evt) {
                        JOptionPane.showMessageDialog(frame, evt.getMessage());
                    }
                }
                // If the user cancelled the operation
                else
                    JOptionPane.showMessageDialog(frame, "the user cancelled the operation");
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                frame.dispose();
                super.windowClosed(e);
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Main.class) {
                    switch (ke.getID()) {

                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_F8) {
                                keyBindPressed = true;

                                try {


                                    if (finalXY == 1) {
                                        finalXY = 0;
                                    } else {
                                        Robot r = new Robot();
                                        String currentDirectory = new File("").getAbsolutePath();
                                        JSONParser parser = new JSONParser();

                                        JSONArray arr = (JSONArray) parser.parse(new FileReader(currentDirectory + "/scripts/" + selectedScript));
                                        JSONObject obj = (JSONObject) arr.get(scriptStep);

                                        int randomizerPlusX = ThreadLocalRandom.current().nextInt(0, 5);
                                        int randomizerPlusY = ThreadLocalRandom.current().nextInt(0, 5);

                                        int randomizerMinusX = ThreadLocalRandom.current().nextInt(0, 5);
                                        int randomizerMinusY = ThreadLocalRandom.current().nextInt(0, 5);

                                        int mouseEnd = (int) (long) obj.get("End");
                                        int mouseSleep = (int) (long) obj.get("Sleep");
                                        int mouseClick = (int) (long) obj.get("MouseClick");
                                        int mouseX = (int) (long) obj.get("MouseX");
                                        int mouseY = (int) (long) obj.get("MouseY");


                                        int currentFrameX = frame.getX();
                                        int currentFrameY =  frame.getY();
                                        //+21 Y to get to OSRS Frame of 800 x 600

                                        int adjustMouseX = (mouseX + currentFrameX) + (randomizerPlusX - randomizerMinusX);
                                        int adjustMouseY = (mouseY + currentFrameY - 23) + (randomizerPlusY - randomizerMinusY);

                                        new MouseMove(adjustMouseX, adjustMouseY, mouseClick);
                                        Thread.sleep(mouseSleep);
                                        scriptStep = scriptStep + 1;
                                        finalXY = 0;
                                        int currentX = MouseInfo.getPointerInfo().getLocation().x;
                                        int currentY = MouseInfo.getPointerInfo().getLocation().y;
                                        System.out.println("Current Point: (" + currentX + ", " + currentY + ")");
                                        System.out.println(currentFrameX +" " + currentFrameY);
                                        r.keyPress(KeyEvent.VK_F9);
                                        r.keyRelease(KeyEvent.VK_F9);
                                        if (mouseEnd == 1) {
                                            finalXY = 1;
                                            scriptStep = 0;
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                System.out.println("Key Pressed");
                            }

                            if (ke.getKeyCode() == KeyEvent.VK_F9) {
                                keyBindPressed = true;

                                try {

                                    if (finalXY == 1) {
                                        finalXY = 0;
                                    } else {
                                        Robot r = new Robot();
                                        String currentDirectory = new File("").getAbsolutePath();
                                        JSONParser parser = new JSONParser();

                                        JSONArray arr = (JSONArray) parser.parse(new FileReader(currentDirectory + "/scripts/" + selectedScript));
                                        JSONObject obj = (JSONObject) arr.get(scriptStep);

                                        int randomizerPlusX = ThreadLocalRandom.current().nextInt(0, 5);
                                        int randomizerPlusY = ThreadLocalRandom.current().nextInt(0, 5);

                                        int randomizerMinusX = ThreadLocalRandom.current().nextInt(0, 5);
                                        int randomizerMinusY = ThreadLocalRandom.current().nextInt(0, 5);
                                        int mouseEnd = (int) (long) obj.get("End");
                                        int mouseSleep = (int) (long) obj.get("Sleep");
                                        int mouseClick = (int) (long) obj.get("MouseClick");
                                        int mouseX = (int) (long) obj.get("MouseX");
                                        int mouseY = (int) (long) obj.get("MouseY");

                                        int currentFrameX = frame.getX();
                                        int currentFrameY =  frame.getY();
                                        //+21 Y to get to OSRS Frame of 800 x 600

                                        int adjustMouseX = (mouseX + currentFrameX) + (randomizerPlusX - randomizerMinusX);
                                        int adjustMouseY = (mouseY + currentFrameY - 23) + (randomizerPlusY - randomizerMinusY);

                                        new MouseMove(adjustMouseX, adjustMouseY, mouseClick);
                                        Thread.sleep(mouseSleep);
                                        scriptStep = scriptStep + 1;
                                        finalXY = 0;
                                        int currentX = MouseInfo.getPointerInfo().getLocation().x;
                                        int currentY = MouseInfo.getPointerInfo().getLocation().y;
                                        System.out.println("Current Point: (" + currentX + ", " + currentY + ")");
                                        System.out.println(currentFrameX +" " + currentFrameY);
                                        r.keyPress(KeyEvent.VK_F8);
                                        r.keyRelease(KeyEvent.VK_F8);

                                        if (mouseEnd == 1) {
                                            finalXY = 1;
                                            scriptStep = 0;
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                System.out.println("Key Pressed");
                            }

                            if (ke.getKeyCode() == KeyEvent.VK_F10) {
                                keyBindPressed = true;
                                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//                                new ItemList("Logs");
                                String currentDirectory = new File("").getAbsolutePath();

                                try {
                                    searchInv(currentDirectory+"/OSRS/sc.png",currentDirectory+"/OSRS/items-icons/1511.png",currentDirectory+"/OSRS/Result.png");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                                int currentX = MouseInfo.getPointerInfo().getLocation().x;
//                                int currentY = MouseInfo.getPointerInfo().getLocation().y;
//                                System.out.println("Current Point: (" + currentX + ", " + currentY + ")");
//                                System.out.println("Key Pressed");
//                                //Gets live screenshot of OSRS Frame only
//
//                                try {
//                                    String currentDirectory = new File("").getAbsolutePath();
//                                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//
////                                    Rectangle screenRectTest = new Rectangle(frame.getX(), frame.getY() +22, applet.getWidth(), applet.getHeight());
////
////                                    BufferedImage capture = new Robot().createScreenCapture(screenRectTest);
//                                    //588, 304 --- 182, 256
//
//                                    Rectangle invRectangle = new Rectangle(frame.getX() +588, frame.getY() +(22+304), 182, 256);
//                                    BufferedImage capture = new Robot().createScreenCapture(invRectangle);
//
//                                    File outputfile = new File("Python/sc.png");
//                                    ImageIO.write(capture, "png", outputfile);
//
//                                    ProcessBuilder pb = new ProcessBuilder("python",currentDirectory + "/Python/main.py","");
//                                    Process p = pb.start();
//
//                                }
//
//                                catch (IOException | AWTException e) {
//
//                                    e.printStackTrace();
//
//                                }


//                                try {
//                                BufferedImage img = new BufferedImage(applet.getWidth(), applet.getHeight(), BufferedImage.TYPE_INT_RGB);
//                                applet.paint(img.getGraphics());
//                                File outputfile = new File("saved.png");
//
//                                    ImageIO.write(img, "png", outputfile);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }

//                                try {
//                                    String currentDirectory = new File("").getAbsolutePath();
//                                    ProcessBuilder pb = new ProcessBuilder("python",currentDirectory + "/Python/getTarget.py","");
//                                    Process p = pb.start();
////                                    Runtime.getRuntime().exec(currentDirectory + "/Python/getTarget.py");
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_F8) {
                                keyBindPressed = false;


                            }

                            if (ke.getKeyCode() == KeyEvent.VK_F9) {
                                keyBindPressed = false;

                            }

                            if (ke.getKeyCode() == KeyEvent.VK_F10) {
                                keyBindPressed = false;

                            }

                            break;
                    }
                    return false;
                }
            }
        });
        //CODE FOR FPS WITH SC
//        long totalMS = 0;
//        int totalFrames = 0;
//        while(true) {
//
//            System.out.println("FPS: " + (float)(1000*totalFrames)/totalMS);
//            totalFrames = 0;
//            totalMS = 0;
////            totalMS = 0;
////            totalFrames = 0;
//            while (totalMS <= 1000) {
//                try {
//
//                    long loopTime = System.currentTimeMillis();
//                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//                    Rectangle screenRectTest = new Rectangle(frame.getX(), frame.getY() + 22, applet.getWidth(), applet.getHeight());
//                    BufferedImage capture = new Robot().createScreenCapture(screenRectTest);
//                    File outputfile = new File("Python/sc.png");
//                    ImageIO.write(capture, "png", outputfile);
//                    long currentMS = System.currentTimeMillis() - loopTime;
//                    totalMS = totalMS + currentMS;
//                    int currentFrame = 1;
//                    totalFrames = totalFrames + currentFrame;
////                    System.out.println("totalFrames: " + totalFrames);
////                    System.out.println("TotalMS: " + totalMS);
//                                    String currentDirectory = new File("").getAbsolutePath();
//                                    ProcessBuilder pb = new ProcessBuilder("python",currentDirectory + "/Python/getTarget.py","");
////                                    Process p = pb.start();
////                                    Runtime.getRuntime().exec(currentDirectory + "/Python/getTarget.py");
//
//                } catch (IOException | AWTException e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//        }

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

    public static void searchInv(String Haystack, String Needle, String Result) throws Exception {
        double averageConfidence = 0;
        int currentArray = 0;
        ArrayList foundNeedleX = new ArrayList<Double>();
        ArrayList foundNeedleY = new ArrayList<Double>();
        ArrayList foundNeedleWidth = new ArrayList<Double>();
        ArrayList foundNeedleHeight = new ArrayList<Double>();
        ArrayList rectList = new ArrayList<Rect>();

        //Declare Array W size.
        int userInv[];
        userInv = new int[27];

        Mat img = Imgcodecs.imread(Haystack);//input image
        if(img.empty())
            throw new Exception("No Haystack");
        Mat tpl = Imgcodecs.imread(Needle);//template image
        if(tpl.empty())
            throw new Exception("No Needle");
        Mat result = new Mat();
        Imgproc.matchTemplate(img, tpl,result,Imgproc.TM_CCOEFF_NORMED);//Template Matching
        Imgproc.threshold(result, result, 0.1, 1, Imgproc.THRESH_TOZERO);

        double threshold = 0.7;

        double maxval;
        Mat dst;

        while(true) {
            Core.MinMaxLocResult maxr = Core.minMaxLoc(result);
            Point maxp = maxr.maxLoc;
            maxval = maxr.maxVal;

            Point maxop = new Point(maxp.x + tpl.width(), maxp.y + tpl.height());
            dst = img.clone();

            if(maxval >= threshold) {

//                foundNeedleX.add(maxp.x);
//                foundNeedleY.add(maxp.y);
//                foundNeedleWidth.add(maxp.x + tpl.width());
//                foundNeedleHeight.add(maxp.y + tpl.height());

                currentArray = currentArray + 1;
                double confidenceLevel = maxval * 100;
                averageConfidence = averageConfidence + confidenceLevel;

                double centerX = maxp.x + (tpl.width()/2);
                double centerY = maxp.y + (tpl.height()/2);
                Point centerPoint = new Point(centerX, centerY);

                System.out.println(centerPoint);

//                System.out.println("[INFO] Needle Found At: " + centerX + ", " + centerY);

                Imgproc.rectangle(img, maxp, new Point(maxp.x + tpl.cols(), maxp.y + tpl.rows()), new Scalar(0, 255, 0),5);
                Imgproc.rectangle(result, maxp, new Point(maxp.x + tpl.cols(),maxp.y + tpl.rows()), new Scalar(0, 255, 0),-1);

            }else{
                break;
            }

        }


        averageConfidence = averageConfidence/(currentArray);
        String sValue = (String) String.format("%.2f", averageConfidence);
        averageConfidence = Double.parseDouble(sValue);

        System.out.println("----------------------------------------");
        System.out.println("[INFO] Total Confidence : " + averageConfidence + "%");
        System.out.println("[INFO] Matches Found: " + (currentArray) );

        System.out.println("----------------------------------------");
        Imgcodecs.imwrite(Result, dst);

//        double match = mmr.minVal;
//        double centerX = matchLoc.x + (template.cols()/2);
//        double centerY = matchLoc.y + (template.rows()/2);
//        System.out.println("Confidence: " + match);
//        System.out.println("Completed with position: (" + centerX + ", " + centerY + ")");



    }

}




class editor extends JFrame implements ActionListener {
    // Text component
    JTextArea t;

    // Frame
    JFrame f;

    // Constructor
    editor()
    {
        // Create a frame

        // Text component
        t = new JTextArea();

        // Create a menubar
        JMenuBar mb = new JMenuBar();

        // Create amenu for menu
        JMenu m1 = new JMenu("File");

        // Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi9 = new JMenuItem("Print");

        // Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi9.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi9);

        // Create amenu for menu
        JMenu m2 = new JMenu("Edit");

        // Create menu items
        JMenuItem mi4 = new JMenuItem("cut");
        JMenuItem mi5 = new JMenuItem("copy");
        JMenuItem mi6 = new JMenuItem("paste");

        // Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);

        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);

        JMenuItem mc = new JMenuItem("close");

        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
        mb.add(mc);

        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
        f.show();


    }



    // If a button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();

        if (s.equals("cut")) {
            t.cut();
        }
        else if (s.equals("copy")) {
            t.copy();
        }
        else if (s.equals("paste")) {
            t.paste();
        }
        else if (s.equals("Save")) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsSaveDialog function to show the save dialog
            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // Create a file writer
                    FileWriter wr = new FileWriter(fi, false);

                    // Create buffered writer to write
                    BufferedWriter w = new BufferedWriter(wr);

                    // Write
                    w.write(t.getText());

                    w.flush();
                    w.close();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
        else if (s.equals("Print")) {
            try {
                // print the file
                t.print();
            }
            catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        else if (s.equals("Open")) {
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser("f:");

            // Invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);

            // If the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // Set the label to the path of the selected directory
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    // String
                    String s1 = "", sl = "";

                    // File reader
                    FileReader fr = new FileReader(fi);

                    // Buffered reader
                    BufferedReader br = new BufferedReader(fr);

                    // Initilize sl
                    sl = br.readLine();

                    // Take the input from the file
                    while ((s1 = br.readLine()) != null) {
                        sl = sl + "\n" + s1;
                    }

                    // Set the text
                    t.setText(sl);
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            // If the user cancelled the operation
            else
                JOptionPane.showMessageDialog(f, "the user cancelled the operation");
        }
        else if (s.equals("New")) {
            t.setText("");
        }
        else if (s.equals("close")) {
            f.setVisible(false);
        }
    }

}

