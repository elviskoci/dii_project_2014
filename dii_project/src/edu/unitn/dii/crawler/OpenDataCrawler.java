package edu.unitn.dii.crawler;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

/**
 * @author Elvis Koci <elvkoci at gmail dot com>
 */

/*
 * This class shows how you can crawl csv and xml files from Open data Trentino 
 * or other web sites on the web and store them in a folder.
 */

public class OpenDataCrawler extends WebCrawler{

	private static final Pattern filters = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz"+"bmp|gif|jpe?g|png|tiff?))$");

	private static final Pattern opdtPatterns = Pattern.compile(".*(\\.(xml|csv?))$");
	
	private static File storageFolder;
	private static String[] crawlDomains;
	
	public static void configure(String[] domain, String storageFolderName) {
		OpenDataCrawler.crawlDomains = domain;

        storageFolder = new File(storageFolderName);
        if (!storageFolder.exists()) {
                storageFolder.mkdirs();
        }
	}
	
	@Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
                return false;
        }

        if (opdtPatterns.matcher(href).matches()) {
        		return true;
        }

        for (String domain : crawlDomains) {
                if (href.startsWith(domain)) {
                        return true;
                }
        }
        return false;
    }
		
	@Override
    public void visit(Page page) {
		
		String url = page.getWebURL().getURL();
		Header[] responseHeaders = page.getFetchResponseHeaders();
		
		// Check if the url matches the specified pattern.
		// We want only to download the dataset of Traditional Products and Typical Recipes
        if (!opdtPatterns.matcher(url).matches()) {
                return;
        }
        
        String lastUpdated="";
        
        // Extract the value of Last_Modified header.
        // This will be compared with the list of existing saved versions for the file 
		if (responseHeaders != null) {
			System.out.println();
			for (Header header : responseHeaders) {
				if(header.getName().compareTo("Last-Modified")==0){
					lastUpdated=header.getValue();
					System.out.println("Last modified: "+lastUpdated);
				}
				
			}
		}
      
		// Extract the substring from the end of the url that represents the filename
		String urlFileName = url.substring(url.lastIndexOf("/")+1);
		
		boolean newVersion=false, prodData= false, recData=false;		
		OpenDataVersions odv;
		try {
			// Check if the version already exists. If exist there is no need to store the file
			odv = OpenDataVersions.getInstance();
			
    		if(urlFileName.matches("prodotti_tradizionali.csv")){
    			
    			if(!odv.existsProductsVersion(lastUpdated)){
    				newVersion=true;
    				prodData=true;
    			}
    		}else if(urlFileName.matches("ricette.xml")){
    			
    			if(!odv.existsRecipesVersion(lastUpdated)){
    				newVersion=true;
    				recData=true;
    			}
    		}else{
    			return;
    		}
    		
    		// If new version the file will be saved on hard disk and the current version will be updated
    		if(newVersion){	
    			//ensure that the filename is unique by adding the timestamp at the end of name
    			String name= urlFileName.substring(0,urlFileName.lastIndexOf("."));
    			String extension = urlFileName.substring(urlFileName.lastIndexOf("."));
    			String fileName= name+"_"+odv.parseStringToDate(lastUpdated).getTime()+""+extension;
    			
    			// store file
	            IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() + "/" + fileName);
	            System.out.println("Stored: " + url);
	            
	            // update versions
	            if(prodData){
	            	odv.addProductsVersion(lastUpdated);
	            }
	            
	            if(recData){
	                odv.addRecipesVersion(lastUpdated);	
	            }
	            
	            //serialise version to permanently save the changes
	            odv.serialiseVersions();
	            
    		}else{
    			System.out.println("Dataset \""+urlFileName+"\" found but not updated since last time");
    		}
    		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
