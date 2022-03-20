package nightmare.utils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by John on 2017/04/24.
 */
public class HttpUtils {

	public static HttpURLConnection createUrlConnection(URL url) throws IOException {
		Validate.notNull(url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		return connection;
	}

	public static String performGetRequest(URL url, boolean withKey) throws IOException {
		return new HttpUtils().performGetRequestWithoutStatic(url, withKey);
	}

	public static String performGetRequest(URL url) throws IOException {
		return new HttpUtils().performGetRequestWithoutStatic(url, false);
	}
	
	public String performGetRequestWithoutStatic(URL url, boolean withKey) throws IOException {
		Validate.notNull(url);

		HttpURLConnection connection = createUrlConnection(url);
		InputStream inputStream = null;
		connection.setRequestProperty("user-agent", "Mozilla/5.0 AppIeWebKit");

		if (withKey) {
			connection.setRequestProperty("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
		}

		String var6;
		try {
			String result;
			try {
				inputStream = connection.getInputStream();
				return IOUtils.toString(inputStream, Charsets.UTF_8);
			} catch (IOException var10) {
				IOUtils.closeQuietly(inputStream);
				inputStream = connection.getErrorStream();
				if (inputStream == null) {
					throw var10;
				}
			}

			result = IOUtils.toString(inputStream, Charsets.UTF_8);
			var6 = result;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		return var6;
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 AppIeWebKit");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
}