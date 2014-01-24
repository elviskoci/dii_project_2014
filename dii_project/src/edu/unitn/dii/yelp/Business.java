package edu.unitn.dii.yelp;

import java.util.ArrayList;

public class Business implements java.io.Serializable, java.lang.Comparable<Business>  {

	// BID Name URL Category Nr_Tips Nr_Reviews 
	// Nr_CheckIns Nr_Users Likes Rating Location

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bid="";
	private String name="";
	private String url="";
	private String phone="";
	private String formated_phone="";
	private String snippet_text="";
	private boolean isClosed=false;
	private ArrayList<String> categories = new ArrayList<String>();
	private int reviews_count=-1;
	private int checkins_count=-1;
	private int users_count=-1;
	private int likes_count=-1;
	private long tips_count=-1;
	private double rating=-1.0;
	private FullAddress fullAddress;
	private ArrayList<Review> reviews = new ArrayList<Review>();

	public Business() {

	}

	public Business(String id, String name, String url,String phone,  ArrayList<String> categories,
			int nr_reviews, double rating, FullAddress loc, ArrayList<Review> review_list) {

		this.bid = id;
		this.name = name;
		this.url = url;
		this.phone = phone;
		this.categories = categories;
		this.reviews_count = nr_reviews;
		this.rating = rating;
		this.fullAddress = loc;
		this.reviews= review_list;
	}
	
	public Business(String id, String name, String url,String phone,String frmt_phone, ArrayList<String> categories,
			int nr_reviews, double rating, FullAddress loc, ArrayList<Review> review_list) {

		this.bid = id;
		this.name = name;
		this.url = url;
		this.phone = phone;
		this.formated_phone=phone;
		this.categories = categories;
		this.reviews_count = nr_reviews;
		this.rating = rating;
		this.fullAddress = loc;
		this.reviews= review_list;
	}
	
	public Business(String id, String name, String url,String phone,boolean is_closed,  ArrayList<String> categories,
			int nr_reviews, double rating, FullAddress loc, ArrayList<Review> review_list) {

		this.bid = id;
		this.name = name;
		this.url = url;
		this.phone = phone;
		this.isClosed=is_closed;
		this.categories = categories;
		this.reviews_count = nr_reviews;
		this.rating = rating;
		this.fullAddress = loc;
		this.reviews= review_list;
	}
	
	public Business(String id, String name, String url,String phone,String frmt_phone,boolean is_closed,
			ArrayList<String> categories,int nr_reviews, double rating, FullAddress loc, String snippet,
			ArrayList<Review> review_list) {

		this.bid = id;
		this.name = name;
		this.url = url;
		this.phone = phone;
		this.formated_phone=frmt_phone;
		this.isClosed=is_closed;
		this.categories = categories;
		this.reviews_count = nr_reviews;
		this.rating = rating;
		this.fullAddress = loc;
		this.snippet_text=snippet;
		this.reviews= review_list;
	}
	
	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public int getReviews_count() {
		return reviews_count;
	}

	public void setReviews_count(int reviews_count) {
		this.reviews_count = reviews_count;
	}

	public int getCheckins_count() {
		return checkins_count;
	}

	public void setCheckins_count(int checkins_count) {
		this.checkins_count = checkins_count;
	}

	public int getUsers_count() {
		return users_count;
	}

	public void setUsers_count(int users_count) {
		this.users_count = users_count;
	}

	public int getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(int likes_count) {
		this.likes_count = likes_count;
	}

	public long getTips_count() {
		return tips_count;
	}

	public void setTips_count(long tips_count) {
		this.tips_count = tips_count;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFormated_phone() {
		return formated_phone;
	}

	public void setFormated_phone(String formated_phone) {
		this.formated_phone = formated_phone;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public FullAddress getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(FullAddress fullAddress) {
		this.fullAddress = fullAddress;
	}
	
	public String getSnippet_text() {
		return snippet_text;
	}

	public void setSnippet_text(String snippet_text) {
		this.snippet_text = snippet_text;
	}

	@Override
	public int compareTo(Business a) {
		
		return this.bid.compareTo(a.bid);
	}

	/*class BusinessesComparator implements Comparator<B> , java.io.Serializable {		
	private static final long serialVersionUID = 1L;

		public int compare(CompleteTip a, CompleteTip b) {
            	return a.getId().compareTo(b.getId());
		}
	}*/
}
