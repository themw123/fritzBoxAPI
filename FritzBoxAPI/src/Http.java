
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Http {
	static String getHttp(String adresse) throws IOException {
		
		URL url = new URL(adresse);
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        
        StringBuilder response = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);
   
        in.close();
        
        inputLine = response.toString();
        
        return inputLine;
        
	}
}
