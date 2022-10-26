
import java.awt.EventQueue;
import java.io.IOException;


import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import org.json.simple.parser.ParseException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.SwingWorker;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;



@SuppressWarnings("serial")
public class AusgabeFenster extends JFrame {

	private JPanel contentPane;
	private static AusgabeFenster frame;
	/**
	 * Launch the application.
	 */
	public static void main(Connection c, Hauptfenster frameH) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AusgabeFenster(c);
					frame.setVisible(true);
					frameH.dispose();
				} catch (Exception e) {
				    JOptionPane.showMessageDialog(null,"Ausgabefenster konnte nicht geöffnet werden.", "Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public AusgabeFenster(Connection c) {
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        exitProcedure(c);
		    }

		});
		setResizable(false);
		setBounds(100, 100, 552, 401);
		setLocationRelativeTo(null);
		setTitle("FritzBox-API");

	
		
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u00F6ffentliche IP: ");
		lblNewLabel.setBounds(10, 10, 102, 19);
		contentPane.add(lblNewLabel);
		//weiter unten ist feld mit ip (txtpnffentlicheIp)
	
		JLabel lblNewLabel_1 = new JLabel(c.getBox());
		lblNewLabel_1.setBounds(10, 78, 518, 49);
		contentPane.add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 107, 518, 222);
		contentPane.add(scrollPane);
		
		JLabel lblNewLabel_2 = new JLabel();
		lblNewLabel_2.setText(c.getDevices());
		scrollPane.setViewportView(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(10, 39, 528, 21);
		contentPane.add(lblNewLabel_3);

		
		JTextPane txtpnffentlicheIp = new JTextPane();
		txtpnffentlicheIp.setBackground(Color.decode("#f0f0f0"));
		String ip = c.getIp();
		txtpnffentlicheIp.setText(ip);
		txtpnffentlicheIp.setToolTipText("");
		txtpnffentlicheIp.setBounds(92, 8, 90, 19);
		contentPane.add(txtpnffentlicheIp);
		
		JButton btnNewButton = new JButton("renew");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button1click(c, txtpnffentlicheIp, lblNewLabel_3, btnNewButton, lblNewLabel);
			}
		});
		btnNewButton.setBounds(185, 12, 74, 15);
		btnNewButton.setToolTipText("Internet wird kurz getrennt");
		contentPane.add(btnNewButton);
		
		if(ip.equals("#error")) {
			btnNewButton.setEnabled(false);
		}
		
		JLabel lblNewLabel_4 = new JLabel();
		lblNewLabel_4.setText("Anzahl: " + c.getAnzahl());
		lblNewLabel_4.setBounds(10, 339, 518, 15);
		contentPane.add(lblNewLabel_4);
		
		JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setText("refresh");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button2click(c, lblNewLabel_2, lblNewLabel_4);
			}
		});
		btnNewButton_1.setIcon(null);
		btnNewButton_1.setBounds(81, 339, 83, 15);
		contentPane.add(btnNewButton_1);


	}
	
	
	private void exitProcedure(Connection c) {
		try {
			c.doLogout();
		} catch (IOException e) {
		    JOptionPane.showMessageDialog(frame,"Das ausloggen von der Fritzbox hat nicht geklappt", "Meldung",JOptionPane.PLAIN_MESSAGE);

		}
		frame.dispose();
	    System.exit(0);
		
	}

	private void button1click(Connection c, JTextPane txtpnffentlicheIp, JLabel lblNewLabel_3, JButton btnNewButton, JLabel lblNewLabel) {
		
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
			
		    	lblNewLabel_3.setText("");
		    	btnNewButton.setEnabled(false);
				c.getNewIp(txtpnffentlicheIp, lblNewLabel_3, btnNewButton, lblNewLabel);

				return null;
			}

		};
		worker.execute();
		
	}
	
	private void button2click(Connection c, JLabel lblNewLabel_2, JLabel lblNewLabel_4) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
			
				lblNewLabel_2.setText(c.getDevices());
				lblNewLabel_4.setText("Anzahl: " + c.getAnzahl());

				return null;
			}

		};
		worker.execute();
	}
}
