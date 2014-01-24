package edu.unitn.dii.hbase;

//make it singleton
public class DBManager {
	private static DBManager instance = null;
	
	protected DBManager() {
	      // Exists only to defeat instantiation.
	 }
	
	public static DBManager getInstance() {
	      if(instance == null) {
	         instance = new DBManager();
	      }
	      return instance;
	}
	
	public void createSchema(){
		
		
	}
	
	
	
}

