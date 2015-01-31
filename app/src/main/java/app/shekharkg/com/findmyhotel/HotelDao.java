package app.shekharkg.com.findmyhotel;

/**
 * Created by SKG on 2/1/2015.
 */
public class HotelDao {

  String imageUrl;
  String name;
  String city;
  int rating;
  String price;

  public HotelDao(String imageUrl, String name, String city, int rating, String price) {
    this.imageUrl = imageUrl;
    this.name = name;
    this.city = city;
    this.rating = rating;
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public int getRating() {
    return rating;
  }

  public String getPrice() {
    return price;
  }
}
