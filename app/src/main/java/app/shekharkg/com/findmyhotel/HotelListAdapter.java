package app.shekharkg.com.findmyhotel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by SKG on 2/1/2015.
 */
public class HotelListAdapter extends BaseAdapter {

  private ArrayList<HotelDao> hotelList;
  private Context context;
  private ViewHolder holder;


  public HotelListAdapter(Context context, ArrayList<HotelDao> hotelList) {
    this.context = context;
    this.hotelList = hotelList;
  }

  @Override
  public int getCount() {
    return hotelList.size();
  }

  @Override
  public Object getItem(int position) {
    return hotelList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    View rowView = convertView;
    if (rowView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      rowView = inflater.inflate(R.layout.hotel_single_row, parent, false);
      holder = new ViewHolder();
      holder.image = (ImageView) rowView.findViewById(R.id.product_image);
      holder.name = (TextView) rowView.findViewById(R.id.name);
      holder.price = (TextView) rowView.findViewById(R.id.price);
      holder.city = (TextView) rowView.findViewById(R.id.city);
      holder.rating = (RatingBar) rowView.findViewById(R.id.rating);
      rowView.setTag(holder);
    } else {
      holder = (ViewHolder) rowView.getTag();
    }

    populateData(position);

    return rowView;
  }

  private void populateData(int position) {
    Ion.with(holder.image).placeholder(R.drawable.loading)
        .error(R.drawable.error).load(hotelList.get(position).getImageUrl());
    holder.name.setText(hotelList.get(position).getName());
    holder.city.setText("City : " + hotelList.get(position).getCity());
    holder.price.setText(hotelList.get(position).getPrice());
    holder.rating.setRating(hotelList.get(position).getRating());
  }

  private class ViewHolder{
    private ImageView image;
    private TextView name;
    private TextView price;
    private TextView city;
    private RatingBar rating;
  }
}
