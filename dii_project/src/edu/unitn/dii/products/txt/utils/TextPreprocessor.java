package edu.unitn.dii.products.txt.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextPreprocessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public   ArrayList<String> getIngredients(String ingredients) throws IOException{
		
		System.out.println("#Raw Ingredients: \n" + ingredients);
		
		ArrayList<String> itemIngredientOne = new ArrayList<String>(Arrays.asList(ingredients.toString().split(";")));
		
		ArrayList<String> itemIngredient = new ArrayList<String>();
		
		Iterator<String> it = itemIngredientOne.iterator();
		
		while(it.hasNext())
		{	
			ArrayList<String> temp = new ArrayList<String>(Arrays.asList(it.next().split(",")));
			itemIngredient.addAll(temp);
		    
		}
		
		int numOfIngredients = itemIngredient.size();
		
		ArrayList<String> finalIngredients = new ArrayList<String>();
		
		for ( int i = 0 ; i < numOfIngredients ; i++){
			System.out.println("#Tokens for : " + itemIngredient.get(i));
			if (itemIngredient.get(i) == "") continue;			
			ArrayList <String> tokens = makeToken(itemIngredient.get(i));
			System.out.println("#Tokens: After stop word removal ");
			System.out.println(tokens);
			tokens = removeNumbers(tokens);
			System.out.println("#Tokens: After number/punctuation removal ");
			System.out.println(tokens);	
			tokens = removeSelectedWords(tokens);
			System.out.println("#Tokens: After selected words removal ");
			System.out.println(tokens);	
			
			//StringUtils.join(list.toArray(),"\t")			
			//while joining need to put some connector like " "
			finalIngredients.add(tokens.toString());
		}
		
		return  finalIngredients;
	}
	
	private ArrayList <String> removeSelectedWords(ArrayList <String> tokens){
		
		//put these words in a text file later
		//String[] selectedWords = {"bicchiere","cucchiaio" ,"gr." ,"gr"};
		
		List<String> selectedWords = new ArrayList<String>();
        
		selectedWords.add("bicchiere");
		selectedWords.add("cucchiaio");
		selectedWords.add("gr.");
		selectedWords.add("gr");
		selectedWords.add("sale");
		tokens.removeAll(selectedWords);		
		return tokens;	
	}
	
	private ArrayList <String> removeNumbers(ArrayList <String> tokens){
		//it removes numbers and puctuations
		Pattern pattern = Pattern.compile("^[0-9_~!@#$%^&*()_+-/ /.]*$");
		for ( int i = 0 ; i < tokens.size(); i++){
			Matcher m = pattern.matcher(tokens.get(i));
			
			if (m.matches()) tokens.remove(i);			
		}
		return tokens;
	}	
	
	private ArrayList<String>  makeToken(String ingredients) throws IOException{
		StringTokenizer st = new StringTokenizer(ingredients); 
		
		Set<String> stopWords = new LinkedHashSet<String>();	
        BufferedReader br = new BufferedReader(new FileReader("resources/stop-words-italian.txt"));
        for(String line;(line = br.readLine()) != null;)
           stopWords.add(line.trim());
        br.close();

        //String[] items = new String[0]; 
		ArrayList<String> tokenList = new ArrayList<String>();        
		
		//int index = 0;
        
        while (st.hasMoreTokens()) {
            
            String t =  st.nextToken().toString();
            
            if(stopWords.contains(t))
            	continue;
            	//System.out.println("stopword found : "  + t);
            else
            	tokenList.add( t);   	            
        }
        return ( tokenList);
        
	}

}
