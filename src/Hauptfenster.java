

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.naming.AuthenticationNotSupportedException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Hauptfenster extends JFrame {
	
	private static Hauptfenster frameH;
	private JPanel contentPane;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frameH = new Hauptfenster();
					frameH.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Hauptfenster() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 552, 300);
		setLocationRelativeTo(null);
		setTitle("FritzBox-API");
		
		
		
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Fritzbox-Verbindung");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 34, 538, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(149, 115, 92, 16);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(251, 117, 118, 17);
		contentPane.add(passwordField);
		
		JLabel lblNewLabel_2 = new JLabel();
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 234, 518, 19);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("verbinden");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 try {
					char[] passChar = passwordField.getPassword();
					String pass = String.valueOf(passChar);
					
					Connection c = new Connection(pass);
				    lblNewLabel_2.setText("Verbindung war erfolgreich. Lade Daten von Fritzbox ...");
					//öffne output
					AusgabeFenster.main(c, frameH);
				    
				} catch (AuthenticationNotSupportedException | NoSuchAlgorithmException | IOException e1) {
				     JOptionPane.showMessageDialog(frameH,"Die Verbindung konnte nicht hergestellt werden. Versuche es erneut und stelle sicher, dass das Passwort das gleiche wie bei der Fritzbox-Oberfläche ist und nicht das Wlan-Passwort.", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(160, 178, 209, 21);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);
		

		
	}
}
