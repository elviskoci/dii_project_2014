package edu.unitn.dii.hbase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.Review;

//make it singleton
public class DBManager {
	private static DBManager instance = null;
	private static final HBaseHelper hbase = HBaseHelper.create();

	protected DBManager() {
		// Exists only to defeat instantiation.
	}

	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	public HTable createOrGetSchema(String tableName) {
		if (hbase == null) {
			error("Couldn't establish connection to HBase");
		}

		HTable table=null;

		try {
//			table = hbase.getOrCreateTable(tableName, "metric", "location",
//					"contact", "review");
			table = hbase.getOrCreateTable(tableName, "business", "location",
					"review");
//			insertDataToHbase(table, new ArrayList<Business>());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return table;
	}

	private static void error(String str) {
		System.out.println("Error: " + str);
	}
	
	public void insertDataToHbase(HTable table, ArrayList<Business> businesses) throws IOException{
		
		Iterator<Business> bitr = businesses.iterator();
		byte[] one = Bytes.toBytes(1);
		byte[] two = Bytes.toBytes(2);
		
		while(bitr.hasNext()){
			Business business = new Business();
			business=bitr.next();
			byte[] rowkey_1 = DigestUtils.md5(business.getFullAddress().getCity());
			byte[] rowkey_2 = one;
			byte[] rowkey_3 = DigestUtils.md5(business.getBid());
			byte[] brk= createCompositeKey(rowkey_1,rowkey_2,rowkey_3, null);
			
			hbase.insertBusiness(table, brk, business);
			
			ArrayList<Review> reviews = business.getReviews();
			Iterator<Review> ritr = reviews.iterator();
			rowkey_2= two;
			while(ritr.hasNext()){
				
				Review review = new Review();
				review = ritr.next();			
				byte[] rowkey_4=DigestUtils.md5(review.getReview_id());
				byte[] rvk= createCompositeKey(rowkey_1,rowkey_2,rowkey_3,rowkey_4);
				hbase.insertReview(table, rvk, review);	
			}
		}
	}
	
	private byte[] createCompositeKey(byte[] rowkey_1, byte[] rowkey_2,
			byte[] rowkey_3, byte[] rowkey_4) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(rowkey_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outputStream.write(rowkey_2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outputStream.write(rowkey_3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (rowkey_4!= null){
			try {
				outputStream.write(rowkey_4);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// byte rowkey[] = outputStream.toByteArray();
		return outputStream.toByteArray();
		/*
		 * StringBuilder sb = new StringBuilder(2 * rowkey.length); for (byte b
		 * : rowkey) { sb.append(String.format("%02x", b & 0xff)); } String
		 * digest = sb.toString();
		 * 
		 * sb = new StringBuilder(2 * rowkey_1.length); for (byte b : rowkey_1)
		 * { sb.append(String.format("%02x", b & 0xff)); } String digest1 =
		 * sb.toString();
		 * 
		 * sb = new StringBuilder(2 * rowkey_2.length); for (byte b : rowkey_2)
		 * { sb.append(String.format("%02x", b & 0xff)); } String digest2 =
		 * sb.toString();
		 * 
		 * sb = new StringBuilder(2 * rowkey_3.length); for (byte b : rowkey_3)
		 * { sb.append(String.format("%02x", b & 0xff)); } String digest3 =
		 * sb.toString();
		 * 
		 * sb = new StringBuilder(2 * rowkey_4.length); for (byte b : rowkey_4)
		 * { sb.append(String.format("%02x", b & 0xff)); } String digest4 =
		 * sb.toString();
		 */
		// System.out.println("digest:" + digest);
		// System.out.println("DIGEST:" + digest1
		// +""+digest2+""+digest3+""+digest4 );

	}

}
