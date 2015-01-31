package app.shekharkg.com.findmyhotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by SKG on 2/1/2015.
 */
public class HotelListActivity extends Activity{

  private ListView listView;
  private ArrayList<HotelDao> hotelList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotels_list);

    listView = (ListView) findViewById(R.id.hotelListView);
    Intent intent = getIntent();
    if(intent != null) {
      Universal U = new Universal();
      new GetSearchResult(this, U.getQuery(intent.getStringExtra(U.CITY),
          intent.getStringExtra(U.CHECK_IN), intent.getStringExtra(U.CHECK_OUT), null, null)).execute(U.URL);
    }
  }

  protected void showResult(String response){
    try {
      JSONObject jsonObject = new JSONObject(response);
      JSONArray hotels = jsonObject.getJSONArray("hotels");
      if(hotels.length() == 0){
        Toast.makeText(this, "No results found.", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Please modify your search and try again.", Toast.LENGTH_LONG).show();
        this.finish();
        return;
      }

      hotelList = new ArrayList<>();
      for(int i=0; i<hotels.length(); i++){
        JSONObject singleHotel = hotels.getJSONObject(i);
        HotelDao hotelDao = new HotelDao(singleHotel.getString("imageURL"),singleHotel.getString("displayName"),singleHotel.getString("city"),
            singleHotel.getInt("starRating"), singleHotel.getString("price"));
        hotelList.add(hotelDao);
      }

      HotelListAdapter adapter = new HotelListAdapter(this, hotelList);
      listView.setAdapter(adapter);

    } catch (JSONException e) {
      e.printStackTrace();
      Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
    }
  }
}
