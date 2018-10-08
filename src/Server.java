import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Server implements Runnable {

    private ServerSocket serverSocket = null;
    private ServerGUI frame;

    private int port;
    private int clientNum = 0;

    private List<Socket> sockets = new ArrayList<Socket>();

    ObjectOutputStream ooss;

    private PaintListener paintListener = new PaintListener() {

        @Override
        public void drawLine(int x, int y, int nx, int ny) {
            Server.this.drawLine(x, y, nx, ny);
        }

    };

    public Server(int port) {
        this.port = port;
        try {
            System.out.println("binding to port " + port + ", please wait  ...");
            serverSocket = new ServerSocket(port);
            System.out.println("server started: " + serverSocket);
            start();
        } catch (IOException ioe) {
            System.out.println("can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("waiting for client " + (clientNum + 1) + " to connect!");

                clientSocket = serverSocket.accept();

                System.out.println("server got connected to a client " + ++clientNum);

                addThread(clientSocket);
            } catch (IOException e) {
                System.out.println("accept failed: " + port);
                System.exit(-1);
            }
        }
    }

    public void start() {
        frame = new ServerGUI(paintListener);
        frame.setVisible(true);
    }

    public void stop() {
    }

    private void addThread(Socket socket) {
        // add new client
        Thread t = new Thread(new ClientHandler(socket, clientNum, frame));
        t.start();
        sockets.add(socket);
    }

    public synchronized void drawLine(int x, int y, int nx, int ny) {
        for (Socket socket : sockets) {
            PrintWriter pw;
            try {
                pw = new PrintWriter(socket.getOutputStream());
                pw.println(String.format("Line %d %d %d %d\n", x, y, nx, ny));
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        Server server = new Server(2222);

        new Thread(server).start();
    }
}

class ClientHandler implements Runnable {
    Socket s;
    int num;
    ServerGUI frame;

    ClientHandler(Socket s, int n) {
        this.s = s;
        num = n;
    }

    ClientHandler(Socket s, int n, ServerGUI frame) {
        this(s, n);
        this.frame = frame;
    }

    public void run() {
        printSocketInfo(s);
        try {
            BufferedInputStream is = new BufferedInputStream(s.getInputStream());
            int n;
            while (true) {
                while ((n = is.available()) > 0) {
                    byte b[] = new byte[n];
                    is.read(b);

                    String s = new String(b, Charset.defaultCharset());

                    if (s.startsWith("Line"))
                        parseLine(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void parseLine(String s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(s);
                while (sc.hasNext()) {
                    sc.next();
                    frame.drawLine(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
                }
                sc.close();
            }

        });
    }

    void printSocketInfo(Socket s) {
        System.out.println("Socket on Server Side:");
        System.out.println(" - Socket on Server " + Thread.currentThread());
        System.out.println(" - Server socket Local Address: " + s.getLocalAddress() + ":" + s.getLocalPort());
        System.out.println(" - Server socket Remote Address: " + s.getRemoteSocketAddress());
    }

}