package app.shekharkg.com.findmyhotel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import java.util.List;

public class GetSearchResult extends AsyncTask<String, Void, String> {

  private Dialog dialog;
  private HotelListActivity hotelListActivity;
  private List<NameValuePair> query;

  public GetSearchResult(HotelListActivity hotelListActivity, List<NameValuePair> query) {
    this.hotelListActivity = hotelListActivity;
    this.query = query;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    dialog = new ProgressDialog(hotelListActivity);
    dialog.setTitle("Loading...");
    dialog.show();
  }

  @Override
  protected String doInBackground(String... params) {
    return GetJsonAsString.GET(params[0],query);
  }

  @Override
  protected void onPostExecute(String result) {
    dialog.dismiss();
    hotelListActivity.showResult(result);
  }
}