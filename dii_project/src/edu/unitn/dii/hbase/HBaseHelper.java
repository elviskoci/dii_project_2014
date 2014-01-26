package edu.unitn.dii.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.Review;

/**
 * 
 * @author Elvis Koci & Bhuvan Shrestha
 * 
 */
public class HBaseHelper {

	private static final Configuration conf = HBaseConfiguration.create();
	//private static final HTablePool tablePool = new HTablePool(conf, Integer.MAX_VALUE);
	
	public static HBaseHelper create() {
		HBaseHelper result = new HBaseHelper();
		try {
			result.hbase = new HBaseAdmin(conf);
			return result;
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private HBaseAdmin hbase;

	static {
		conf.set("hbase.master", "localhost:60000");
	}

	private HBaseHelper() {

	}	

	public HTable createTable(String tableName, String... descriptors)
			throws IOException {
		if (tableExists(tableName)) {
			dropTable(tableName);
		}
		return doCreateTable(tableName, descriptors);
	}

	private HTable doCreateTable(String tableName, String... descriptors)
			throws IOException {
		HTableDescriptor descriptor = new HTableDescriptor(tableName);
		for (String each : descriptors) {
			HColumnDescriptor cd = new HColumnDescriptor(each.getBytes());
			descriptor.addFamily(cd);
		}
		
		hbase.createTable(descriptor);
		debug(String.format("Database %s created", tableName));
		return new HTable(conf, tableName);
	}
	
    private static void debug(Object obj) {
        System.out.println(String.format("### DEBUG: %s", obj.toString()));
    }    

	public void dropTable(String tableName) throws IOException {
		hbase.disableTable(tableName);
		hbase.deleteTable(tableName);
	}

	public HTable getOrCreateTable(String tableName, String... descriptors)
			throws IOException {
		if (!tableExists(tableName)) {
			doCreateTable(tableName, descriptors);
		}
		return getTable(tableName);
	}
	
	public HTable getTable(String tableName) throws IOException {
		
		return new HTable(conf, tableName);
	}

	public void insert(HTable table, byte[] rowKey, List<String> values)
			throws IOException {
		if (values.size() == 3) {
			Put put = new Put(rowKey);
			put.add(Bytes.toBytes(values.get(0)), Bytes.toBytes(values.get(1)),
					Bytes.toBytes(values.get(2)));
			table.put(put);
		}
	}

	public void insert(HTable table, String rowKey, String prefix, String qualifier, byte[] value)
			throws IOException {
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(prefix), Bytes.toBytes(qualifier), value);
		table.put(put);
	}
	
	//Prepare single Put and return it for further processing.
	public Put createSinglePut(HTable table, byte[] rowKey, List<String> values)
			throws IOException {
		
		Put put= new Put();
		
		if (values.size() == 3) {
			put = new Put(rowKey);
			put.add(Bytes.toBytes(values.get(0)), Bytes.toBytes(values.get(1)),
					Bytes.toBytes(values.get(2)));
		}	
		return put;
	}
	
	//Create single put similar as the method above.
	//The input parameters are given in a different way
	public Put createSinglePut(HTable table, String rowKey, String prefix, String qualifier, byte[] value)
			throws IOException {
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(prefix), Bytes.toBytes(qualifier), value);
		
		return put;
	}
	
	// Add more columns to an existing put
	public Put appendToPut(Put put, String prefix, String qualifier, byte[] value)
			throws IOException {
		
		put.add(Bytes.toBytes(prefix), Bytes.toBytes(qualifier), value);
		return put;
	}
	
	public void insertReview(HTable table, byte[] rowKey, Review review)
			throws IOException {
		//insert code for puting reviews
	}
	
	public Boolean insertBusiness(HTable table, byte[] rowKey, Business business)
			throws IOException {
		
		String tempRow = "";
		try{
			 //delete later..not needed
			
			Put put = new Put(rowKey);
			put.add(Bytes.toBytes("business"), Bytes.toBytes("name"),
					Bytes.toBytes(business.getName()));
			
	//		CATEGORIES
			ArrayList<String> categories= business.getCategories();
			String concat_categories="";
			for(int i=0;i<categories.size();i++){
				concat_categories=concat_categories+categories.get(i)+",";
			}
			concat_categories=concat_categories.substring(0, concat_categories.length());
			tempRow = " category: " + concat_categories;
			put.add(Bytes.toBytes("business"), Bytes.toBytes("categories"),
					Bytes.toBytes(concat_categories));
			
	//		CONTACTS
			put.add(Bytes.toBytes("business"), Bytes.toBytes("phone"), 
					Bytes.toBytes(business.getPhone()));
			put.add(Bytes.toBytes("business"), Bytes.toBytes("url"),
					Bytes.toBytes(business.getUrl()));
			put.add(Bytes.toBytes("business"), Bytes.toBytes("formated phone"),
					Bytes.toBytes(business.getFormated_phone()));
	
	//		METRICS
			put.add(Bytes.toBytes("business"), Bytes.toBytes("rating"),
					Bytes.toBytes(business.getRating()));
			put.add(Bytes.toBytes("business"), Bytes.toBytes("checkins count"),
					Bytes.toBytes(business.getCheckins_count()));
			put.add(Bytes.toBytes("business"), Bytes.toBytes("reviews count"),
					Bytes.toBytes(business.getReviews_count()));
			
			tempRow = tempRow + " rating: " + business.getRating();
			tempRow = tempRow + " checkins_count: " + business.getCheckins_count();
			tempRow = tempRow + " Review count: " + business.getReviews_count();
			
			
			
			//Foursquare API returns always null; 	
			put.add(Bytes.toBytes("business"), Bytes.toBytes("tips count"),
					Bytes.toBytes(business.getTips_count()));
			
			//data not provided by Foursquare API.
			//put.add(Bytes.toBytes("metrics"), Bytes.toBytes("likes count"),
			//		Bytes.toBytes(business.getLikes_count()));
			
			//Should this be put into reviews?
			//put.add(Bytes.toBytes("business"), Bytes.toBytes("snippet text"),
			//		Bytes.toBytes(business.getSnippet_text()));
			
	//		LOCATION
			
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("address"),
//					Bytes.toBytes(business.getFullAddress().getAddress()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("formated address"),
//					Bytes.toBytes(business.getFullAddress().getDisplay_address()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("city"),
//					Bytes.toBytes(business.getFullAddress().getCity()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("state"),
//					Bytes.toBytes(business.getFullAddress().getState()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("state code"),
//					Bytes.toBytes(business.getFullAddress().getState_code()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("country"),
//					Bytes.toBytes(business.getFullAddress().getCountry()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("country code"),
//					Bytes.toBytes(business.getFullAddress().getCountry_code()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("postal"),
//					Bytes.toBytes(business.getFullAddress().getPostal_code()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("latitude"),
//					Bytes.toBytes(business.getFullAddress().getLatitude()));
//			put.add(Bytes.toBytes("location"), Bytes.toBytes("longitude"),
//					Bytes.toBytes(business.getFullAddress().getLongitude()));
//			
			table.put(put);
		}catch (NullPointerException e){
			
			System.out.println("NULL VALUE WAS FOUND");
			return false;
		}
		System.out.println(tempRow);
		return true;
	}
	
//	public void putBusinessLoop(HTable table, String rowKey, Business business)
//			throws IOException {
//		
//		Field[] fields= Business.class.getDeclaredFields();
//		Business.class.
//		table.put(put);
//	}
//	
	public boolean tableExists(String tableName) {
		try {
			return hbase.tableExists(tableName);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return false;
	}
}
