
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class getArrayJson {
	 
	
	
	static String[] getArray (String path) throws FileNotFoundException, IOException, ParseException{
		
		String zuJson = Http.getHttp(path);
		
		JSONParser parser = new JSONParser();  
		JSONObject json = (JSONObject) parser.parse(zuJson);
		JSONArray json2 = (JSONArray) json.get("network");
		String[] a = new String[json2.size()];
		
		
		for(int i = 0; i<json2.size();i++) {
			JSONObject ipx = (JSONObject) json2.get(i);
			
			String ip = (String) ipx.get("ip");
			String name = (String) ipx.get("name");
			String active = (String) ipx.get("active");
			String wlan = (String) ipx.get("wlan_UIDs");
			
			//
			
			a[i] = active + "," + ip +"\t  " + name + " " + wlan;
			
			
		}
		
		/*
		for(int i = 0; i<a.length;i++) {
			System.out.println(a[i]);
		}
		*/
		
		
		
		//länge von neuem array bestimmen mit nur activen geräten
		int zaehler = 0;
		for(int i = 0; i<a.length;i++) {
			String s = a[i];
			
			if (s.substring(0,1).equals("1")) {
				zaehler++;
			}
		
		}
		
		
		
		//neues a2 array befüllen mit nur activen geräten ohne die 1 am anfang
		String[] a2 = new String[zaehler];
		int counter2 = 0;
		for(int i = 0; i<a.length;i++) {
			String s = a[i];
			int laengevons = s.length();
			if (s.substring(0,1).equals("1")) {
				
				String neu = a[i].substring(2, laengevons);
				a2[counter2]= neu;
				counter2++;
			}
		
		}
		
	
	
		
		
		//array sortieren nach größe
		schleife:
		for(int i = 0; i<a2.length;i++) {
			for(int k = 1; k<a2.length;k++) {
				
				if(i > k) {
					k = i+1;
				}
				
				
				if(i == a2.length-1) {
					break schleife;
				}
				

				
				int stelle1 = getStelle1(a2, i);
				int stelle2 = getStelle2(a2, k);
				
				if(stelle1 > stelle2) {
					String alt = a2[i];
					a2[i] = a2[k];
					a2[k] = alt;
					
				}
			}
			
		}
		
		//fritzbox also die .1 loeschen
		for(int i = 0; i<a2.length-1;i++) {
			a2[i] = a2[i+1];
		}
		a2[a2.length-1] = null;
		
		//neues array ohne letzten null eintrag erstellen
		String[] a3 = new String[a2.length-1];
		for(int i = 0; i<a3.length;i++) {
			a3[i] = a2[i];
		}
		
		
		
		//gucken ob es ein wlan gerät ist
		for(int i = 0; i<a3.length;i++) {
			String s = a3[i];
			int laengevons = s.length();
			
			if(s.contains("wlan")) {
				int stelle = s.indexOf("wlan");
				String entfernen = s.substring(stelle, laengevons);

				String neu = a3[i].replace(entfernen, ""); 
				laengevons = neu.length();
				int anzahll = 60 - laengevons;
				
				try {
					String hilf = neu.substring(12,15);
					@SuppressWarnings("unused")
					int test = Integer.parseInt(hilf);
					
					anzahll++;
				}
				catch (Exception e){
				}
				
				
				for(int j = 0; j<anzahll;j++) {
					
					neu = neu + " ";
				}
				neu = neu + "ja";
				
				a3[i] = neu;
				
				//System.out.println(a[i]);
				
			}
		
		}
		
		
		return a3;
		
		
		
		
		/*
		for(int i = 0; i<a2.length;i++) {
			System.out.println(a2[i]);
		}
		*/
	
		/*
		String zuJson = getHttp(path);
		
		JSONParser parser = new JSONParser();  
		JSONObject json = (JSONObject) parser.parse(zuJson);  
	

		 System.out.println(json);
		 
		 ArrayList list = new ArrayList();
         JSONArray slideContent = (JSONArray) json.get("network");
         
         Iterator i = slideContent.iterator();
         
         while (i.hasNext()) {
             JSONObject slide = (JSONObject) i.next();
             list.add((String)slide.get("name") +  " - " + (String)slide.get("ip")+  " - " + (String)slide.get("active"));
         }
         
         System.out.println(list);
		 */
	}
	
	

	
	private static int getStelle1(String a[], int i) {
		
		int stelle1 = 0;
		
		try {
			String stelle = a[i].substring(12, 15);
			stelle1 = Integer.parseInt(stelle);
		}
		catch(Exception e) {
			
			try {
				String stelle = a[i].substring(12, 14);
				stelle1 = Integer.parseInt(stelle);
			}
			catch(Exception e2) {
				String stelle = a[i].substring(12, 13);
				stelle1 = Integer.parseInt(stelle);
			} 
			
		}
		return stelle1;
		
		
	}
	
	private static int getStelle2(String[] a, int i) {
		
		int stelle2;
		
		try {
			String stelle2x = a[i].substring(12, 15);
			stelle2 = Integer.parseInt(stelle2x);
		}
		catch(Exception e) {
			
			try {
				String stelle2x = a[i].substring(12, 14);
				stelle2 = Integer.parseInt(stelle2x);
			}
			catch(Exception e2) {
				String stelle2x = a[i].substring(12, 13);
				stelle2 = Integer.parseInt(stelle2x);
			} 
			
		}
		return stelle2;
	}
	
	
}
