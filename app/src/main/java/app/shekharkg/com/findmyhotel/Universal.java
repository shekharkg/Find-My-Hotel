package app.shekharkg.com.findmyhotel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SKG on 2/1/2015.
 */
public class Universal {
  protected final String CITY = "city";
  protected final String CHECK_IN = "check_in";
  protected final String CHECK_OUT = "check_out";
  protected final String PROPERTY_TYPE = "property_type";
  protected final String HOTELS = "Hotels";
  protected final String LAT = "lat";
  protected final String LNG = "lng";
  protected final String IsLatLngSearch = "isLatLngSearch";
  protected final String URL = "http://180.92.168.7/hotels";

  protected List<NameValuePair> getQuery(String city, String checkIn, String checkOut, String lat, String lng, boolean isLatLngSearch){
    List<NameValuePair> query = new ArrayList<NameValuePair>(2);
    if(!isLatLngSearch)
      query.add(new BasicNameValuePair("location", city));
    else{
      query.add(new BasicNameValuePair("lat", lat));
      query.add(new BasicNameValuePair("lng", lng));
    }
    query.add(new BasicNameValuePair("checkin", checkIn));
    query.add(new BasicNameValuePair("checkout", checkOut));
    query.add(new BasicNameValuePair("property_type", "Hotels"));
    return query;
  }

}
