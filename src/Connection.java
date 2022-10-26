

import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.naming.AuthenticationNotSupportedException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigInteger;
import java.io.IOException;


public class Connection {
	
	private String password, grundAdresse, luaAdresse, challenge, response, loginAdresse, sid, devices[], ipAdresse;

	
	Connection (String password) throws IOException, NoSuchAlgorithmException, AuthenticationNotSupportedException {
		this.password = password;
		this.grundAdresse = "http://fritz.box/";
		this.luaAdresse = "login_sid.lua";
		
		this.challenge = getChallenge();
		this.response = getResponse();
		
		this.loginAdresse = getLoginAdresse();

		
		this.sid = getSid();
		
	
	}
	
	
	String getIp(){
		
		
		
		String ip ="";
		int count = 0;
		do {
			try {
				String httpInhalt = Http.getHttp(grundAdresse + "internet/inetstat_monitor.lua?sid=" + sid);
				if(httpInhalt.contains("IPv4-Adresse: ")) {
					ip = httpInhalt.substring(httpInhalt.indexOf("IPv4-Adresse: ") + 12);
					ip = ip.substring(2, ip.indexOf("</span></div></td></tr><tr"));
				}
				else if (httpInhalt.contains("IP-Adresse: ")){
					ip = httpInhalt.substring(httpInhalt.indexOf("IP-Adresse: ") + 12);
					ip = ip.substring(0, ip.indexOf("</span></div></td></tr><tr"));
				}
					
			}
			catch(Exception e) {
				ip = "#error";
				if(count == 7) {
				    JOptionPane.showMessageDialog(null,"Die IP konnte nicht bezogen werden, starten Sie das Programm neu.", "Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
	        
	        count++;
	        if(ip.equals("#error") || ip.contains("typeof") || ip.equals(null) || ip.equals("")) {
		        try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        if(count > 7) {
			    JOptionPane.showMessageDialog(null,"Die IP konnte nicht bezogen werden, starten Sie das Programm neu.", "Error",JOptionPane.ERROR_MESSAGE);
	        	ip = "#error";
	        	break;
	        }

		} while (ip.equals("#error") || ip.contains("typeof") || ip.equals(null) || ip.equals(""));
		
		ipAdresse = ip;
		
		return ip;
	}
	
	
	
	String getDevices(){
		
		String devicesString = "";
		
		try {
			String path = grundAdresse + "query.lua?sid=" + sid + "&network=landevice:settings/landevice/list(name,ip,dhcp,wlan,ethernet,active,manu_name,online,wlan_UIDs,wlan_station_type,vendorname)";
			this.devices = getArrayJson.getArray(path);
			
			//array in string packen
			for(int i = 0; i<devices.length;i++) {
				devicesString = devicesString + devices[i] + "<br>";
			}
			devicesString = "<html><pre>" + devicesString + "<pre></html>";
			
			
		} catch (IOException | ParseException e) {
			 devicesString = "#error";
		    JOptionPane.showMessageDialog(null,"Die Geräte konnten nicht bezogen werden", "Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		
		return devicesString;
		
	}
	
	int getAnzahl(){
		
		int anzahl = 0;
		
		try {
			String path = grundAdresse + "query.lua?sid=" + sid + "&network=landevice:settings/landevice/list(name,ip,dhcp,wlan,ethernet,active,manu_name,online,wlan_UIDs,wlan_station_type,vendorname)";
			this.devices = getArrayJson.getArray(path);
			anzahl = devices.length;
		} catch (IOException | ParseException e) {
			
		}
		
		return anzahl;
		
	}

	
	void getNewIp(JTextPane txtpnffentlicheIp, JLabel lblNewLabel_3, JButton btnNewButton, JLabel lblNewLabel ){
	
		try {
			
			lblNewLabel_3.setText("Einen Moment. Neue IP wird angefordert .... ");
			String ipOld = ipAdresse;
			
			
			//disconnect und dadurch neue IP
			String path = grundAdresse + "/internet/inetstat_monitor.lua?sid=" + sid + "&myXhr=1&action=disconnect&useajax=1";
			Http.getHttp(path);
	        
			
	        //warte 10 sek
			TimeUnit.SECONDS.sleep(10);
			String ipNew = this.getIp();
	       
			
		    if(ipOld.equals(ipNew)) {
		    	lblNewLabel_3.setText("Deine IP lautet immernoch " + ipNew + ". Versuche es erneut.");
		    }
		    else {
		    	txtpnffentlicheIp.setText(ipNew);
		    	lblNewLabel_3.setText("IP wurde erneuert. (alte IP: " + ipOld + ")");
				/*
				TimeUnit.SECONDS.sleep(4);	
				lblNewLabel_3.setText("");
				*/
		    }
		    
		    btnNewButton.setEnabled(true);
	        
		} catch ( InterruptedException | IOException e1) {
		    JOptionPane.showMessageDialog(null,"Erneuerung der IP funktioniert nicht", "Error",JOptionPane.ERROR_MESSAGE);
		    btnNewButton.setEnabled(true);
		    lblNewLabel_3.setText("");
			e1.printStackTrace();
		}

        
    }
    
    /*Test
	String getSysLog() throws IOException {
		String httpInhalt = Http.getHttp(grundAdresse + "data.lua?sid=" + sid + "&lang=de&page=log&no_sidrenew=");
		return httpInhalt;
	}
	*/
	
	
	
	
	
	
	
	
	private String getChallenge() throws IOException {
        
		String adresse = grundAdresse + luaAdresse;
        String httpInhalt = Http.getHttp(adresse);
        
        int anfang = httpInhalt.indexOf("<Challenge>")+11;
        int ende = httpInhalt.indexOf("</Challenge>");
        
        String challenge = httpInhalt.substring(anfang, ende);
        // String challenge = inputLine.substring();
        
        return challenge;
        
	}
	
	private String getResponse() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String kombination = challenge + "-" + password;
		String kombinationBytes = new String(kombination.getBytes("UTF-16LE"), "UTF-8");
		
		//md5
		MessageDigest digest;
		digest = MessageDigest.getInstance("MD5");
		digest.update(kombinationBytes.getBytes());
		final byte byteData[] = digest.digest();
		final BigInteger bigInt = new BigInteger(1, byteData);
		String md5 = bigInt.toString(16);
		
		kombination = challenge + "-" + md5;
		
		return kombination;
		
		
	}
	
	private String getLoginAdresse() {
		return grundAdresse + luaAdresse + "?response=" + response + "&username=fritz2832";
	}
	
	
	private String getSid() throws IOException, AuthenticationNotSupportedException {
		
        String httpInhalt = Http.getHttp(loginAdresse);

        int anfang = httpInhalt.indexOf("<SID>")+5;
        int ende = httpInhalt.indexOf("</SID>");
        
        String sid = httpInhalt.substring(anfang, ende);
        // String challenge = inputLine.substring();
        
        if (sid.equals("0000000000000000")) {
			throw new AuthenticationNotSupportedException("Login nicht möglich. Falsches Passwort!");
		}
        
        return sid;
        
	}
	
	
	void doLogout() throws IOException {
		URL url = new URL(grundAdresse + luaAdresse + "?sid=" + sid + "&logout=1");
        URLConnection uc = url.openConnection();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        in.close();
		

	}
	
	public String getBox() {
		
		String box = "<html><pre><font face=\"Arial\" size=\"3\" >IP                                    Name                                                                                      Wlan</font><br>";
		box = box + "<pre></html>";
		return box;
		
	}
	
	
	
}

