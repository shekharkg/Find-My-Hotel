package app.shekharkg.com.findmyhotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by SKG on 2/1/2015.
 */
public class HotelListActivity extends Activity{



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotels_list);

    Intent intent = getIntent();
    if(intent != null) {
      Universal U = new Universal();
      new GetSearchResult(this, U.getQuery(intent.getStringExtra(U.CITY),
          intent.getStringExtra(U.CHECK_IN), intent.getStringExtra(U.CHECK_OUT), null, null)).execute(U.URL);
    }
  }

  protected void showResult(String msg){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }
}
