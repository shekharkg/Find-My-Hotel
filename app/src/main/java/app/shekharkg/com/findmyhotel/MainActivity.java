package app.shekharkg.com.findmyhotel;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

  private Calendar calendar;
  private Button actionCheckIn, actionCheckOut, actionSearch;
  private Dialog dialog;
  private DatePicker datePicker;
  private boolean isCheckInDialog;

  // isLatLngSearch is used to determine search is going to take place using city name or using lat lng value.
  private static boolean isLatLngSearch;
  private EditText cityNameET;
  private ImageButton actionLatLng;
  private static String lat, lng;
  private static GoogleMap map;
  private static MapDialog mapDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
  }

  //Getting references of views
  private void initView() {
    calendar = Calendar.getInstance();
    actionCheckIn = (Button) findViewById(R.id.checkIn);
    actionCheckOut = (Button) findViewById(R.id.checkOut);
    actionSearch = (Button) findViewById(R.id.searchButton);
    cityNameET = (EditText) findViewById(R.id.selectCity);
    actionLatLng = (ImageButton) findViewById(R.id.chooseOnMapButton);
    populateData();
    initAction();
  }

  //Display initial values to views
  private void populateData() {
    actionCheckIn.setText("Check In Date : " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
        (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));

    actionCheckOut.setText("Check Out Date : " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
        (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));

    isLatLngSearch = false;
  }

  //Applying listeners to views
  private void initAction() {
    actionCheckIn.setOnClickListener(this);
    actionCheckOut.setOnClickListener(this);
    actionSearch.setOnClickListener(this);
    actionLatLng.setOnClickListener(this);
    cityNameET.setOnClickListener(this);
  }

  //Display date picker dialog
  public void showDatePickerDialog(String checkInOut, int yr, int mn, int day) {
    dialog = new Dialog(this);
    dialog.setTitle(checkInOut);
    dialog.setContentView(R.layout.date_picker_dialog);
    datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
    datePicker.setCalendarViewShown(false);
    datePicker.updateDate(yr, mn, day);

    //For limiting the date in date picker
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    Date date = null;
    try {
      String strData = yr+"-"+(mn+1)+"-"+day + " 00:00:00";
      if(isCheckInDialog)
        strData = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
      date = sdf.parse(strData);

    } catch (ParseException e) {
      e.printStackTrace();
    }
    if(date != null)
      datePicker.setMinDate(date.getTime());

    dialog.setCancelable(false);
    dialog.findViewById(R.id.cancelButton).setOnClickListener(this);
    dialog.findViewById(R.id.saveButton).setOnClickListener(this);
    dialog.show();
  }

  @Override
  public void onClick(View v) {

    String minDateOut = actionCheckIn.getText().toString().split(":")[1];

    switch (v.getId()){

      //Check in date button is clicked
      case R.id.checkIn:
        isCheckInDialog = true;
        showDatePickerDialog("Check Out Date", Integer.parseInt(minDateOut.split("/")[2].trim()),
            Integer.parseInt(minDateOut.split("/")[1].trim()) - 1, Integer.parseInt(minDateOut.split("/")[0].trim()));
        break;

      //Check out date button is clicked
      case R.id.checkOut:
        isCheckInDialog = false;
        showDatePickerDialog("Check Out Date", Integer.parseInt(minDateOut.split("/")[2].trim()),
            Integer.parseInt(minDateOut.split("/")[1].trim()) - 1, Integer.parseInt(minDateOut.split("/")[0].trim()) + 1);
        break;

      //cancelling the date picker dialog
      case R.id.cancelButton:
        dialog.dismiss();
        datePicker = null;
        dialog = null;
        break;

      //Selecting date
      case R.id.saveButton:
        datePicked();
        break;

      //Search for result
      case R.id.searchButton:
        validateSearchData();
        break;

      //Option to choose lat lng using google map
      case R.id.chooseOnMapButton:
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        mapDialog = new MapDialog();
        mapDialog.setRetainInstance(true);
        mapDialog.show(fm, "map_dialog");
        break;

      //Used to toggle between Lat Lang wise search and city wise search
      case R.id.selectCity:
        isLatLngSearch = false;
        break;
    }
  }


  //DialogFragment to choose Lan Lng using google maps
  public static class MapDialog extends DialogFragment implements GoogleMap.OnMapLoadedCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.map_dialog, container);
    }

    @Override
    public void onDestroyView(){
      super.onDestroyView();
      Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
      FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
      ft.remove(fragment);
      ft.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      try{
        map.setMyLocationEnabled(true);
        map.setOnMapLoadedCallback(this);
        map.setOnMapClickListener(this);
        map.setOnInfoWindowClickListener(this);
      }catch (Exception e){
        e.printStackTrace();
      }
    }

    @Override
    public void onMapLoaded() {
      Location location = map.getMyLocation();
      if (location != null) {
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
      }
    }

    @Override
    public void onMapClick(LatLng latLng) {
      map.clear();
      Marker marker =  map.addMarker(new MarkerOptions().position(latLng).title("Touch to select city"));
      marker.showInfoWindow();
      lat = latLng.latitude+"";
      lng = latLng.longitude+"";
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
      Toast.makeText(getActivity(),"Location selected.", Toast.LENGTH_LONG).show();
      isLatLngSearch = true;
    }
  }


  //Before start searching, validating search data are correct or not
  private void validateSearchData() {
    String city = cityNameET.getText().toString();
    if(!isLatLngSearch && city.length() < 1){
      Toast.makeText(this,"Please enter city name or select in map.",Toast.LENGTH_LONG).show();
      return;
    }
    String checkInDate = actionCheckIn.getText().toString().split(":")[1];
    checkInDate = checkInDate.split("/")[0].trim() + "/" + checkInDate.split("/")[1].trim() + "/" + checkInDate.split("/")[2].trim();

    String checkOutDate = actionCheckOut.getText().toString().split(":")[1];
    checkOutDate = checkOutDate.split("/")[0].trim() + "/" + checkOutDate.split("/")[1].trim() + "/" + checkOutDate.split("/")[2].trim();

    //If search data is correct then start new activity to display results
    Intent searchResult = new Intent(this, HotelListActivity.class);
    Universal U = new Universal();
    searchResult.putExtra(U.CITY,city);
    searchResult.putExtra(U.CHECK_IN,checkInDate);
    searchResult.putExtra(U.CHECK_OUT,checkOutDate);
    searchResult.putExtra(U.PROPERTY_TYPE,U.HOTELS);
    searchResult.putExtra(U.LAT,lat);
    searchResult.putExtra(U.LNG,lng);
    searchResult.putExtra(U.IsLatLngSearch,isLatLngSearch);
    startActivity(searchResult);
  }


  //Changing view data when date is picked
  private void datePicked() {
    if(isCheckInDialog)
      actionCheckIn.setText("Check In Date : " + datePicker.getDayOfMonth() + "/" +
          (datePicker.getMonth() + 1) + "/" + datePicker.getYear());

    actionCheckOut.setText("Check Out Date : " + datePicker.getDayOfMonth() + "/" +
        (datePicker.getMonth() + 1) + "/" + datePicker.getYear());

    actionSearch.setVisibility(View.VISIBLE);

    dialog.dismiss();
    datePicker = null;
    dialog = null;
    actionCheckOut.setEnabled(true);
  }

}
