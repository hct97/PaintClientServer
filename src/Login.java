import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPanel, cPanel;
    private JTextField nameField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Login() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 100, 450, 100);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        JLabel labelClient = new JLabel("Client");
        labelClient.setFont(new Font("Tahoma", Font.BOLD, 20));
        contentPanel.add(labelClient);

        cPanel = new JPanel();
        cPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel lblEnterYourName = new JLabel("Enter Your Name");
        lblEnterYourName.setPreferredSize(new Dimension(110, 20));
        cPanel.add(lblEnterYourName);
        nameField = new JTextField();
        cPanel.add(nameField);
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(80, 20));
        cPanel.add(loginButton);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.LINE_AXIS));
        contentPanel.add(cPanel);

        // when the button is pressed, send the name to the client application
        // and connect to the server
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clientLogin();
            }
        });

        // allow for hitting enter to send a chat message
        nameField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    clientLogin();
                }
            }
        });
    }

    private void clientLogin() {
        String name = nameField.getText();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					Client client = new Client("localhost", name, 2222);
//                    Client client = new Client("192.168.1.142", name, 2222);
                    new Thread(client).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // dispose of this login page
                dispose();
            }
        });
    }
}