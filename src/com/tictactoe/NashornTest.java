package com.tictactoe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class NashornTest {
	
	public void testJavaScript() {
		// Quick test for manipulating the JavaScript Engine
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			Object happy = jse.eval("var happy = 2+5; happy");
			System.out.println(happy.toString());
			
			// So we can invoke a script outside using this line of functions
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		    engine.eval(new FileReader("math.js"));
		    Invocable invocable = (Invocable) engine;
		    Object result = invocable.invokeFunction("function2", 50);
		    System.out.println(result);
		    
		    Object result2 = invocable.invokeFunction("nextfunction", new int[] {10, 11, 12});
		    System.out.println(result2);
		    
		    Object result3 = invocable.invokeFunction("spacefunction", 20, 30);
		    System.out.println(result3);
		    
		    Object result4 = invocable.invokeFunction("stringfunction");
		    System.out.println(result4);
			
		}catch(ScriptException | FileNotFoundException | NoSuchMethodException e) {
			// Code smell pass
		}
	}
	
	public String testURL(String url) {
		URL oracle;
		String fullString = "";
		try {
			oracle = new URL(url);
			BufferedReader in = new BufferedReader(
	        new InputStreamReader(oracle.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	            //System.out.println(inputLine);
	        	fullString += inputLine + "\n";
	        in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fullString;
	}

	public static void main(String[] args) {

		NashornTest cool = new NashornTest();
		System.out.println("-----------------------------");
		cool.testJavaScript();
		System.out.println("-----------------------------");
		System.out.println(cool.testURL("https://raw.githubusercontent.com/ctomni231/cwtactics/master/credits.json"));
		System.out.println("-----------------------------");
	}

}
