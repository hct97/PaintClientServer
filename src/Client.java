import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client implements Runnable {
    private Socket socket = null;
    private DataOutputStream os = null;
    private String username;
    private ClientGUI frame;

    public Client(String ipAddr, String username, int serverPort) {
        this.username = username;

        // set up the socket to connect to the gui
        try {
            socket = new Socket(ipAddr, serverPort);
            start();
        } catch (UnknownHostException h) {
            JOptionPane.showMessageDialog(new JFrame(), "Unknown Host " + h.getMessage());
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "IO exception: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        // check for a new message
        // once we receive it, send it to the server
        try {
            os = new DataOutputStream(socket.getOutputStream());

            String info = "";
            info += "Client [" + username + "] has joined.\n";
            info += "Client socket Local Address: " + socket.getLocalAddress() + ":" + socket.getLocalPort() + "\n";
            info += "Client socket Remote Address: " + socket.getRemoteSocketAddress();
            System.out.println(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String toBinaryString(int b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    public void start() throws IOException {
        frame = new ClientGUI(username, new PaintListener() {
            @Override
            public void drawLine(int x, int y, int nx, int ny) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            os.write(String.format("Line %d %d %d %d\n", x, y, nx, ny).getBytes());
                            os.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        frame.setVisible(true);

        new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner sc = null;
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    sc = new Scanner(dis);

                    while (true) {
                        while (sc.hasNextLine()) {
                            String s = sc.nextLine();
                            if (s.startsWith("Line")) {
                                SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        String[] ss = s.trim().split("\\s+");
                                        int x = Integer.parseInt(ss[1]);
                                        int y = Integer.parseInt(ss[2]);
                                        int nx = Integer.parseInt(ss[3]);
                                        int ny = Integer.parseInt(ss[4]);
                                        frame.drawLine(x, y, nx, ny);
                                    }

                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void stop() {
    }

}