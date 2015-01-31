package app.shekharkg.com.findmyhotel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.List;

public class GetJsonAsString {
  public static String GET(String url,List<NameValuePair> query){
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(url);

    try {
      httppost.setEntity(new UrlEncodedFormEntity(query));
      HttpResponse response = httpclient.execute(httppost);
      if(response.getStatusLine().getStatusCode() != 200)
        return "Something went wrong. Please try again.";
      return EntityUtils.toString(response.getEntity());

    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
    } catch (IOException e) {
      // TODO Auto-generated catch block
    }
    return null;
  }
}