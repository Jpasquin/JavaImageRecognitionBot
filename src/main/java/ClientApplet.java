import java.applet.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public final class ClientApplet extends JPanel implements AppletStub {

    private static final long serialVersionUID = 5627929030422355843L;
    private Applet applet = null;
    private URLClassLoader ClassLoader = null;
    private URL codeBase = null, documentBase = null;
    private HashMap<String, String> parameters = new HashMap<>();
    private static final Pattern codeRegex = Pattern.compile("code=(.*?) ");
    private static final Pattern archiveRegex = Pattern.compile("archive=(.*?) ");
    private static final Pattern parameterRegex = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");

    public ClientApplet(String root, String params, int Width, int Height) {
        try {
            this.setLayout(new BorderLayout(0, 0));
            String pageSource = downloadPage(new URL(root + params), null);
            Matcher codeMatcher = codeRegex.matcher(pageSource);
            Matcher archiveMatcher = archiveRegex.matcher(pageSource);

            if (codeMatcher.find() && archiveMatcher.find()) {
                String archive = archiveMatcher.group(1);
                String jarLocation = root + "/" + archive;
                String code = codeMatcher.group(1).replaceAll(".class", "");
                Matcher parameterMatcher = parameterRegex.matcher(pageSource);
                this.codeBase = new URL(jarLocation);
                this.documentBase = new URL(root);

                while (parameterMatcher.find()) {
                    this.parameters.put(parameterMatcher.group(1), parameterMatcher.group(2));
                }

                this.ClassLoader = new URLClassLoader(new URL[]{new URL(jarLocation)});
                this.applet = (Applet) ClassLoader.loadClass(code).newInstance();
                this.applet.setStub(this);
                this.applet.setPreferredSize(new Dimension(Width, Height));
                this.add(this.applet, BorderLayout.CENTER);


            }

        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Loading.. Please Check Your Internet Connection.", "Error Loading..", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ClassLoader getClassLoader() {
        return this.ClassLoader;
    }

    public String postRequest(URL Address, String UserAgent, HashMap<String, String> data) {
        StringBuilder parameterBuilder = new StringBuilder();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        int i = 0;

        while(it.hasNext() && i < data.entrySet().size() - 1) {
            Entry<String, String> e = it.next();
            parameterBuilder.append(e.getKey()).append("=").append(e.getValue()).append("&");
            ++i;
        }
        Entry<String, String> entry = it.next();
        parameterBuilder.append(entry.getKey()).append("=").append(entry.getValue());

        String parameterURL = parameterBuilder.toString();
        if (parameterURL == null || parameterURL.trim().isEmpty()) {
            return null;
        }

        try {
            URLConnection Connection = Address.openConnection();
            Connection.addRequestProperty("Protocol", "HTTP/1.1");
            Connection.addRequestProperty("Connection", "keep-alive");
            Connection.addRequestProperty("Keep-Alive", "300");

            if (UserAgent != null) {
                Connection.addRequestProperty("User-Agent", UserAgent);
            } else {
                Connection.addRequestProperty("User-Agent", "Mozilla/5.0 (" + System.getProperty("os.name") + " " + System.getProperty("os.version") + ") Java/" + System.getProperty("java.version"));
            }

            Connection.setDoOutput(true);


            try (OutputStreamWriter writer = new OutputStreamWriter(Connection.getOutputStream())) {
                writer.write(parameterURL);
                writer.flush();
                String Line;
                StringBuilder Builder = new StringBuilder();
                try (BufferedReader Reader = new BufferedReader(new InputStreamReader(Connection.getInputStream()))) {
                    while ((Line = Reader.readLine()) != null) {
                        Builder.append(Line).append("\n");
                    }
                }
                return Builder.toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadPage(URL Address, String UserAgent) {
        try {
            URLConnection Connection = Address.openConnection();
            Connection.addRequestProperty("Protocol", "HTTP/1.1");
            Connection.addRequestProperty("Connection", "keep-alive");
            Connection.addRequestProperty("Keep-Alive", "300");
            if (UserAgent != null) {
                Connection.addRequestProperty("User-Agent", UserAgent);
            } else {
                Connection.addRequestProperty("User-Agent", "Mozilla/5.0 (" + System.getProperty("os.name") + " " + System.getProperty("os.version") + ") Java/" + System.getProperty("java.version"));
            }

            String Line;
            StringBuilder Builder = new StringBuilder();
            try (BufferedReader Reader = new BufferedReader(new InputStreamReader(Connection.getInputStream()))) {
                while ((Line = Reader.readLine()) != null) {
                    Builder.append(Line).append("\n");
                }
            }
            return Builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Applet getApplet() {
        return this.applet;
    }

    public void start() {
        if (this.applet != null) {
            this.applet.init();
        }

        if (this.applet != null) {
            this.applet.start();
        }
    }

    public void destruct() {
        if (this.applet != null) {
            this.remove(this.applet);
            this.applet.stop();
            this.applet.destroy();
            this.applet = null;
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return this.documentBase;
    }

    @Override
    public URL getCodeBase() {
        return this.codeBase;
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    @Override
    public void appletResize(int width, int height) {
    }
}