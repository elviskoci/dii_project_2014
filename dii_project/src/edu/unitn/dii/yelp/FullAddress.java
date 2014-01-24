package edu.unitn.dii.yelp;

public class FullAddress implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private String display_address;
	private String city;
	private String state;
	private String country;
	private String state_code;
	private String postal_code;
	private String country_code;
	private double latitude;
	private double longitude;

	public FullAddress() {

	}
	
	public FullAddress(String address, String city, String postal_code, double lat, double lng) {

		this.address = address;
		this.city = city;
		this.postal_code = postal_code;
		this.latitude=lat;
		this.longitude=lng;
	}
	
	public FullAddress(String address, String city, 
			String state , String country, String postal_code, double lat, double lng) {

		this.address = address;
		this.city = city;
		this.postal_code = postal_code;
		this.latitude=lat;
		this.longitude=lng;
		this.state=state;
		this.country=country;
	}
	
	public FullAddress(String address, String formated_address, String city,
			String state_code, String postal_code) {

		this.address = address;
		this.display_address = formated_address;
		this.city = city;
		this.postal_code = postal_code;
		this.state_code = state_code;
	}
	
	public FullAddress(String address, String formated_address, String city,
			String state_code, String country_code, String postal_code) {

		this.address = address;
		this.display_address = formated_address;
		this.city = city;
		this.postal_code = postal_code;
		this.state_code = state_code;
		this.country_code=country_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDisplay_address() {
		return display_address;
	}

	public void setDisplay_address(String display_address) {
		this.display_address = display_address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
