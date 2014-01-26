package edu.unitn.dii.yelp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import edu.unitn.dii.foursquare.PointsOfInterest;

public class Yelp implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private static final String consumerKey = "0s3dTw_R0XBpcQCkWY1ElA";
	private static final String consumerSecret = "8Gw_JVHu5sqnrE80ULLDOdvLPPg";
	private static final String token = "pBK0yxBYQN4xzlOAdw_IY3u1FWRVWPnZ";
	private static final String tokenSecret = "rKGXQkK09jIndjh7Gt1_FQrGeqc";
	private transient OAuthService service;
	private transient Token accessToken;
	private TreeMap<Business, Integer> yelp_results= new  TreeMap<Business, Integer>();
	private int query_count=1;
	private int total_review_count=0;
	
	private Yelp() {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	public String search(String term, double latitude, double longitude) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	public String boundingBoxSearch(double sw_latitude, double sw_longitude,
			double ne_latitude, double ne_longitude, int limit) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("lang", "it");
		request.addQuerystringParameter("bounds", sw_latitude + ","
				+ sw_longitude + "|" + ne_latitude + "," + ne_longitude);
		request.addQuerystringParameter("limit", "" + limit);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	public String radiousSearch(String latitude, String longitude, int radious, int l) {

		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		request.addQuerystringParameter("radius_filter", "" + radious);
		request.addQuerystringParameter("limit", ""+l);
//		localflavor,restaurants,food,italian,eventservices,hotelstravel,breweries,bars
//		request.addQuerystringParameter("category_filter",
//				"localflavor,restaurants,food,italian,eventservices,hotelstravel,breweries,bars");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	public String getBusiness(String business) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/business/" + business);
//		request.addQuerystringParameter("lang_filter", "it");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	public String parseJSONValue(JSONObject json, String element) {

		String value = "null";

		try {
			value = json.getString(element);
		} catch (JSONException e) {}

		return value;
	}

	public JSONArray parseJSONArray(JSONObject json, String element) {

		JSONArray array = new JSONArray();

		try {
			array = json.getJSONArray(element);
		} catch (JSONException e) {}

		return array;
	}
	
	public JSONObject parseJSONObject(JSONObject json, String element) {

		JSONObject obj = null;

		try {
			obj = json.getJSONObject(element);
		} catch (JSONException e) {}

		return obj;
	}
	
	public void getTrentoYelpData(ArrayList<String> pois, int radious, int limit) throws JSONException{

		Iterator<String> queries = pois.iterator();
		String latlng = "", result = "";

		while (queries.hasNext()) {
			System.out
					.println("\n\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n"
							+ "Query nr." + this.query_count);
			latlng = queries.next();
			String[] coordinates = latlng.split(",");
			result = radiousSearch(coordinates[0], coordinates[1], radious, limit);
			System.out.println("\n" + result + "\n");

			JSONObject resJson = new JSONObject(result);
			
			JSONArray businesses = parseJSONArray(resJson, "businesses"); 
			ArrayList<String> businessIds = new ArrayList<String>();
			String bid="";
			if(businesses.length()!=0){
				for (int i = 0; i < businesses.length(); i++) {
					JSONObject business = businesses.getJSONObject(i);
					bid =parseJSONValue(business,"id");
					businessIds.add(bid);
				}
			}else{
				System.out.println("NO RESULTS WERE RETURNED FROM THE QUERY");
				this.query_count++;
				continue;
			}
			
			ArrayList<JSONObject> businessReviews = new ArrayList<JSONObject>();
			for (int j = 0; j < businessIds.size(); j++) {
				// System.out.println(j + ". " + businessIds.get(j));
				businessReviews.add(new JSONObject(getBusiness(businessIds.get(j))));
			}

			Iterator<JSONObject> bt = businessReviews.iterator();
			JSONArray reviews = new JSONArray();
			JSONArray categories= new JSONArray();
			JSONArray address = new JSONArray();
			JSONArray formated_address= new JSONArray();
			JSONObject location = new JSONObject() ;
			JSONObject completeBusiness = new JSONObject();
			JSONObject review = new JSONObject();
			String  id = "", name = "", val="", adr="",frmt_adr="",
					city="", state_code="", country_code="", postal_code="",
					phone="", display_phone="",url="";
			String reviewId="", review_text="", snippet_text="";
			int nr_reviews=-1;
			double business_rating=-1.0, review_rating=-1.0;
			long createdAt=0;
			boolean is_closed=false;
			
			while (bt.hasNext()) {
				
				completeBusiness = bt.next();
				System.out.println("\n=======================================================");
				System.out.println(completeBusiness.toString()+"\n");
				id = parseJSONValue(completeBusiness, "id");
				name = parseJSONValue(completeBusiness, "name");
				phone = parseJSONValue(completeBusiness, "phone");
				display_phone = parseJSONValue(completeBusiness, "display_phone");
				url = parseJSONValue(completeBusiness, "url");
				val=parseJSONValue(completeBusiness,"review_count");
				if(val.compareTo("null")!=0)
				nr_reviews = Integer.parseInt(val);
				
				val=parseJSONValue(completeBusiness,"rating");
				if(val.compareTo("null")!=0)
				business_rating = Double.parseDouble(val);
				
				val=parseJSONValue(completeBusiness,"is_closed");
				if(val.compareTo("null")!=0)
				is_closed = Boolean.parseBoolean(val);
				
				System.out.println("Business id: "+id);
				System.out.println("Business name: "+name.toUpperCase());
				System.out.println("Business url: "+url);
				System.out.println("Business phone: "+phone);
				System.out.println("Business display: "+display_phone);
				System.out.println("Business review count: "+nr_reviews);
				System.out.println("Business rating: "+business_rating);
				System.out.println("Business is closed: "+is_closed);
				System.out.print(  "Business categories:  ");
				
				ArrayList<String>  ctgrs = new ArrayList<String>();
				categories = parseJSONArray(completeBusiness, "categories");
				for (int c = 0; c < categories.length(); c++) {
					JSONArray cat = categories.getJSONArray(c);
					ctgrs.add(cat.getString(0));
					System.out.print("\t"+cat.getString(0));
				}
				
				location = parseJSONObject(completeBusiness,"location");
				if(location!=null){
					address = parseJSONArray(location, "address");
					adr="";
					for(int m=0;m<address.length();m++ ){
						adr=address.getString(m)+adr+", ";
					}
					//adr=adr.substring(0,adr.lastIndexOf(","));
					formated_address= parseJSONArray(location, "display_address");
					frmt_adr="";
					for(int l=0;l<formated_address.length();l++ ){
						frmt_adr=frmt_adr+formated_address.getString(l)+"\n";
					}
					city = parseJSONValue(location, "city");
					if(city.compareTo("")==0){
						city="unknown";
					}
					state_code= parseJSONValue(location, "state_code");
					country_code =parseJSONValue(location, "country_code");
					postal_code=parseJSONValue(location,"postal_code");
					System.out.println("\nBusiness Location: "+ adr+ ", "+city+", "+state_code+", "+postal_code);
					System.out.println("Business Formated Address: "+ frmt_adr);
				}else{
					System.out.println("\nBusiness Location: NULL");
				}
				snippet_text= parseJSONValue(completeBusiness, "snippet_text");
				System.out.println("Business Snippet Text: "+snippet_text);
				ArrayList<Review> business_review_list = new ArrayList<Review>();
				
				reviews = parseJSONArray(completeBusiness, "reviews");
				for (int k = 0; k < reviews.length(); k++) {
					review = new JSONObject(reviews.getString(k));
					reviewId= parseJSONValue(review,"id");
					review_text=parseJSONValue(review, "excerpt");
					val=parseJSONValue(review,"rating");
					if(val.compareTo("")!=0)
					review_rating = Double.parseDouble(val);
					val=parseJSONValue(review,"time_created");
					if(val.compareTo("")!=0)
					createdAt = Long.parseLong(val);					
					System.out.println("Review"+(k + 1)+" ID: "+ reviewId);
					System.out.println("Review"+(k + 1)+" Text: "+ review_text);
					System.out.println("Review"+(k + 1)+" Rating: "+ review_rating);
					System.out.println("Review"+(k + 1)+" Date: "+ (new Date(createdAt)).toString());
					System.out.println();
					business_review_list.add(new Review(reviewId, review_text, createdAt, review_rating));
				}
				Business b = new Business(id, name, url, phone,display_phone, is_closed, 
						ctgrs, nr_reviews, business_rating,
						new FullAddress(adr,frmt_adr, city,state_code,country_code, postal_code),
						snippet_text,business_review_list);
				
				if(!this.yelp_results.containsKey(b)){
					this.yelp_results.put(b, 1);
					
					int tmp=0;
					if(b.getReviews_count()>3){
						tmp=3;
					}
					else{
						tmp=b.getReviews_count();
					}
					this.total_review_count=this.total_review_count+tmp;
				
				}else{
					int rep=this.yelp_results.get(b);
					this.yelp_results.put(b, rep+1);
				}
			}
			this.query_count++;
		}
		
		BusinessRepetitionComparator brc = new  BusinessRepetitionComparator(this.yelp_results);
		TreeMap<Business, Integer> sorted_businesses = new TreeMap<Business,Integer>(brc);
		sorted_businesses.putAll(this.yelp_results);
		System.out.println(sorted_businesses);
		System.out.println("\nTotal number of unique businesses retieved from yelp: "+this.yelp_results.size());
		System.out.println("Total number of reviews retieved from yelp: "+this.total_review_count);
	}
	
	class BusinessRepetitionComparator implements Comparator<Business> , java.io.Serializable {
		private static final long serialVersionUID = 1L;
		Map< Business, Integer> base;
	    public BusinessRepetitionComparator(Map<Business,Integer> base) {
	        this.base = base;
	    }
	    
	    public int compare(Business a, Business b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
	}

	 // Serialise the YelpData object.
	public void serialiseYelpData(String file) throws IOException{
			
	  	/*String formatString="dd_MMM_yyyy";
		DateFormat df= new SimpleDateFormat(formatString, Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formatedDate= df.format(new Date());*/
		
        FileOutputStream fileOut =
        new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        System.out.println("\nSerialized data is saved in: "+file);
	}
		
	// Deserialise the YelpData object.
	public static Yelp deserialiseYelpData(String file) throws IOException, ClassNotFoundException{
		
	    File serFile=  new File(file);//./storage/yelp_output.ser
	    if(!serFile.exists()){
	    	System.out.println("Serialization file does not exist. Create new YELP object");
	    	return new Yelp();
	    }
	    
	    Yelp obj = null;
	    FileInputStream fileIn = new FileInputStream(file);
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    obj= (Yelp) in.readObject();
	    in.close();
	    fileIn.close();
	    System.out.println("The file was deserialized: "+file);
	    return obj;
	}
	    
	public OAuthService getService() {
		return service;
	}

	public void setService(OAuthService service) {
		this.service = service;
	}

	public Token getAccessToken() {
		
		return accessToken;
	}

	public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}

	public TreeMap<Business, Integer> getYelp_results() {
		return yelp_results;
	}

	public static void main(String[] args) {

		boolean deserialize =false;
		Yelp yelp = new Yelp();
				
		if(deserialize){
			try {
				yelp=deserialiseYelpData("./storage/yelp_output_1500.ser");
				yelp.setService(new ServiceBuilder().provider(YelpApi2.class)
						.apiKey(consumerKey).apiSecret(consumerSecret).build());
				yelp.setAccessToken(new Token(token, tokenSecret));			
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   
		ArrayList<String> pois = new ArrayList<String>();

		pois.addAll(PointsOfInterest.GEO_POINTS);
		// pois.add("46.071444,11.126722");

		try {
			yelp.getTrentoYelpData(pois, 500, 20);
			yelp.serialiseYelpData("./storage/yelp_output.ser");
		} catch (JSONException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
