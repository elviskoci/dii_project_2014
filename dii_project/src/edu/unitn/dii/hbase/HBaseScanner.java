package edu.unitn.dii.hbase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseScanner {

	public static void main(String[] args) {
		byte[] rowkey_1 = DigestUtils.md5("Unknown");
		
		//byte[] rowkey_2 = Bytes.toBytes(1);
		//byte[] rowkey_3 = DigestUtils.md5("2-giganti-trento");
		//byte[] rowkey_4=DigestUtils.md5(review.getReview_id());
		
		//byte[] rowkey = createCompositeKey( rowkey_1 ,rowkey_2 , null, null);
		
		
		
		
		//byte[] stopkey = DigestUtils.md5("Unknown");
		
		
		//System.out.println("Key used to query: " + getHexKey(rowkey));
		
		HTable table = null;
		try {
			table = DBManager.hbase.getOrCreateTable("BusinessReview", "business", "location","review");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		byte[] singleByte = DigestUtils.md5("z");
		System.out.println(""+singleByte.length);
	    
		int x =Bytes.toInt(rowkey_1);
		int y=x+1;
		byte[] stopvalue = rowkey_1;
	//	stopvalue[0]= Bytes.toBytes(y);
		
		System.out.println(getHexKey(rowkey_1)+"\n"+(getHexKey(stopvalue)));
		//System.out.println(rowkey_1.toString()+"\n"+stopFilter.toString());
		//System.out.println(getHexKey(rowkey_1)+"\n"+(getHexKey(stopFilter)));
		//byte[] stopFilter = DigestUtils.md5("T");
		
		Filter filter1 = new RowFilter( CompareOp.LESS_OR_EQUAL,
				new BinaryComparator(stopvalue));
		Scan scan = new Scan (rowkey_1,filter1);
		
		ResultScanner scanner = null;
		try {
			scanner = table.getScanner(scan);
			
			//scanner.
			
			
			
			
			
			int count = 0 ;
			
//			for (Result result = scanner.next(); result != null; result = scanner.next())
//			{
//				//byte[] row = result.getRow();
//				
//				count++;
//				
//				//System.out.println((result.getValue(Bytes.toBytes("business"),Bytes.toBytes( "phone"))).toString());
////				for(KeyValue keyValue : result.list()) {
////			        System.out.println("Qualifier : " + keyValue.getKeyString() + " : Value : " + Bytes.toString(keyValue.getValue()));
////			    }
			    
		//	}
			Iterator<Result> itr = scanner.iterator();
			
		while(itr.hasNext()){								
				itr.next();
				count++;
				
			}
			
			System.out.println( count);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			scanner.close();  // always close the ResultScanner!
			
		}
		
		//table.close();
		

	}
	
	
	private static String getHexKey(byte[] rowkey) {
		 StringBuilder sb = new StringBuilder(2 * rowkey.length); 
		 for (byte b: rowkey) { 
			 sb.append(String.format("%02x", b & 0xff)); 
			 }
		 String  digest = sb.toString();
				  
				 
				  
		return digest;
	}
	
	
	
	private static byte[] createCompositeKey(byte[] rowkey_1, byte[] rowkey_2,
			byte[] rowkey_3, byte[] rowkey_4) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(rowkey_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (rowkey_2!= null){
			try {
				outputStream.write(rowkey_2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (rowkey_3!= null){
			try {
				outputStream.write(rowkey_3);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (rowkey_4!= null){
			try {
				outputStream.write(rowkey_4);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		return outputStream.toByteArray();
		

	}
	

}

/*
  Scan scan = new Scan(Bytes.ToBytes("a.b.x|1"),Bytes.toBytes("a.b.x|2"); //creating a scan object with start and stop row keys

scan.setFilter(colFilter);//set the Column filters you have to this scan object.

//And then you can get a scanner object and iterate through your results
ResultScanner scanner = table.getScanner(scan);
for (Result result = scanner.next(); result != null; result = scanner.next())
{
    //Use the result object
}*/
