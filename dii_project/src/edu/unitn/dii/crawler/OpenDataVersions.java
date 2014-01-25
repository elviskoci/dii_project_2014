package edu.unitn.dii.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import com.sleepycat.persist.impl.Format;

public class OpenDataVersions implements java.io.Serializable {
	
	//implement class as singleton
	private static OpenDataVersions instance = null;
	 
	private static final long serialVersionUID = 1L;
	private ArrayList<Date> recipesVersions = new ArrayList<Date>(); 
	private ArrayList<Date> productsVersions= new ArrayList<Date>(); 

	private OpenDataVersions(){
		
	}
	
	// create a date object parsing the string representation of the date 
	public Date parseStringToDate(String strDate) throws ParseException{
		String formatString="EEE, dd MMM yyyy HH:mm:ss z";
		DateFormat df= new SimpleDateFormat(formatString, Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = df.parse(strDate);
		
		return date;
	}
	
	// get a formated (IETF) string representation of the date
	public String formatDateToString(Date date){
		
		// IETF data format. This is the format used by the Open Data Trentino website. 
		String formatString="EEE, dd MMM yyyy HH:mm:ss z";
		DateFormat df= new SimpleDateFormat(formatString, Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formatedDate= df.format(date);
		
		return formatedDate;
	}
	
	// Check if recipes dataset version exists. Two options: using string representation of date or date object
	public boolean existsRecipesVersion(String strDate) throws ParseException{

		Date date=parseStringToDate(strDate);
		return getRecipesVersions().contains(date);
	}
	
	public boolean existsRecipesVersion(Date date) throws ParseException{		
		
		return getRecipesVersions().contains(date);
	}
	
	// Check if traditional products dataset version exists. Two options: using string representation of date or date object
	public boolean existsProductsVersion(String strDate) throws ParseException{

		Date date=parseStringToDate(strDate);
		return getProductsVersions().contains(date);
	}
	
	public boolean existsProductsVersion(Date date) throws ParseException{
		
		return getProductsVersions().contains(date);
	}
	
	// Get current version of traditional products dataset. Two options : in string format or a date object
	public Date getCurrentProductsVersionDate(){
		
		int size = getProductsVersions().size();
		return getProductsVersions().get(size-1);
	}
	
	public String getCurrentProductsVersionString(){
		
		int size = getProductsVersions().size();
		return formatDateToString(getProductsVersions().get(size-1));
	}
	
	
	// Get current version of traditional products dataset. Two options : in string format or a date object
	public Date getCurrentRecipesVersionDate(){
		
		int size = getRecipesVersions().size();
		return getRecipesVersions().get(size-1);
	}
	
	public String getCurrentRecipesVersionString(){
		
		int size = getRecipesVersions().size();
		return formatDateToString(getRecipesVersions().get(size-1));
	}
	
	// Serialise the OpenDataVersions object into the specified directory.
	public void serialiseVersions() throws IOException{
		
        FileOutputStream fileOut =
        new FileOutputStream("./storage/versions.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
        System.out.println("Serialized data is saved in /storage/versions.ser");
	}
	
	// Deserialise the OpenDataVersions object.
	private static OpenDataVersions deserialiseVersions() throws IOException, ClassNotFoundException{
		
		OpenDataVersions versions = null;
	   
	    FileInputStream fileIn = new FileInputStream("./storage/versions.ser");
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    versions = (OpenDataVersions) in.readObject();
	    in.close();
	    fileIn.close();
	 
	    return versions;
	}
	
	// Add a new version of traditional products dataset
	public boolean addProductsVersion(String strDate) throws ParseException{
		
		Date date = parseStringToDate(strDate);
		if(getProductsVersions().contains(date)){
			System.out.println("Traditional products version not added already exist: "+strDate);
			return false;
		}
		
		getProductsVersions().add(date);
		System.out.println("New version of traditional products dataset was added: "+strDate);
		return true;		
	}
	
	// Add a new version of typical recipes dataset
	public boolean addRecipesVersion(String strDate) throws ParseException{
		
		Date date = parseStringToDate(strDate);
		if(getRecipesVersions().contains(date)){
			System.out.println("Recipes version not added already exist: "+strDate);
			return false;
		}
		
		getRecipesVersions().add(date);
		System.out.println("New version of typical recipes dataset was added: "+strDate);
		return true;		
	}
	
	// return an iterator to access all the saved versions of traditional products dataset
	public Iterator<Date> getAllRecipesVersions(){
		
		return getRecipesVersions().iterator();
	}
	
	// return an iterator to access all the saved versions of typical recipes dataset
	public Iterator<Date> getAllProductsVersions(){
		
		return getProductsVersions().iterator();
	}
	
	private ArrayList<Date> getRecipesVersions() {
		return recipesVersions;
	}

	private void setRecipesVersions(ArrayList<Date> recipeVersions) {
		this.recipesVersions = recipeVersions;
	}

	private ArrayList<Date> getProductsVersions() {
		return productsVersions;
	}

	private void setProductsVersions(ArrayList<Date> productsVersions) {
		this.productsVersions = productsVersions;
	}
	
	// Implement class as singleton 
	public static OpenDataVersions getInstance() throws ClassNotFoundException, IOException{
	      if(instance == null) {
	    	  File file = new File("./storage/versions.ser");
	    	  if(file.exists()){
	    		  instance = deserialiseVersions();  
	    	  }else{
	    		  OpenDataVersions odv= new OpenDataVersions();
	    		  instance=odv;
	    		  odv.serialiseVersions();
	    	  }    	  
	      }
	      return instance;
	}
	 
	public static void main(String args[]){
		
		/*
		 * Change manually the current version. This was used for testing purposes.
		 */	
		try {
			OpenDataVersions currVer= OpenDataVersions.getInstance();
		
			currVer.getProductsVersions().clear();
			currVer.getRecipesVersions().clear();
			
			currVer.getProductsVersions().add(new Date());
			currVer.getRecipesVersions().add(new Date());
			
			currVer.serialiseVersions();
			
		} catch (ClassNotFoundException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e2){
			e2.printStackTrace();
		}
			
			
		
		try {
			
			OpenDataVersions odv= OpenDataVersions.getInstance();
			
			Iterator<Date> itrProd= odv.getAllProductsVersions();
			
			System.out.println("\nList of product versions:");
			
			while(itrProd.hasNext()){
				
				System.out.println(odv.formatDateToString(itrProd.next()));
			}
			
			Iterator<Date> itrRec= odv.getAllRecipesVersions();
			
			System.out.println("\nList of recipes versions:");
			
			while(itrRec.hasNext()){
				
				System.out.println(odv.formatDateToString(itrRec.next()));
			}
			
		} catch (ClassNotFoundException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e2){
			e2.printStackTrace();
		}
		
		
	}
}
