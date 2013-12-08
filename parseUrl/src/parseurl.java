import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class parseurl {

	
	public static void main(String[] args) {
		
		String addr = getAddr("COAF0119");
		System.out.println(addr);

	}
	
	static String getAddr(String num){
		String url_from = "http://www.i-organic.org.tw/farm/" + num;
		String html_content;
		String addr = "";
		try {
			// get URL content
			URL url = new URL(url_from);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
 
			String inputLine;
			StringBuffer response = new StringBuffer();
			 
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();
			html_content = response.toString();
			//System.out.println(response.toString());
			int start = html_content.indexOf("olspan='5'>經營業者地址：");
			String sub_html_content = html_content.substring(start);
			int finish = sub_html_content.indexOf("<");
			addr = sub_html_content.substring(18, finish);
			return addr;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addr;
	}
}

