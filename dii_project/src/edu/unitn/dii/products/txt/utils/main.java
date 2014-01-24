package edu.unitn.dii.products.txt.utils;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println ("Main");
		processString();
		
}
	private static void processString() throws IOException {

		
		String[] rawIngredientsList = {"1 cotechino di maiale trentino, 500 gr. di fagioli secchi " +
				";1 bicchiere di vino rosso "+
				";1 cipolla bianca"+
				";1 carota gialla "+
				";1 costa di sedano "+
				";2 foglie d'alloro "+
				";concentrato di pomodoro, olio, sale, aglio, prezzemolo. ;" , "250 gr. di farina bianca"+
		";50 gr. di farina saracena"+ 
		";1/2 l. di latte "+
		";1 cucchiaio d'olio"+ 
		";200 gr. di lucanica cauriota fresca "+
		";30 gr. di pancetta affumicata trentina "+
		";30 gr. di lardo trentino, sale e pepe. ;"};
				
		ArrayList<ArrayList<String>> processedIngredientList = new ArrayList<ArrayList<String>>();
		
		TextPreprocessor textPreProcessor = new TextPreprocessor();
		
		for ( int i = 0 ; i < rawIngredientsList.length ; i++){
			System.out.println("#Recipe");
			processedIngredientList.add(textPreProcessor.getIngredients(rawIngredientsList[i]));
			//System.out.println( "Ingredients: " + textPreProcessor.getIngredients(rawIngredientsList[i]));
		}
	
	}

}
