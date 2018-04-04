package com.zeusas.dp.ordm.service;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;

import com.zeusas.dp.ordm.entity.Item;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Java8Nashorn {
	   ScriptEngineManager scriptEngineManager ;
	      ScriptEngine nashorn; 
	      ScriptEngine engine ;  
	
	@Before
	public void init(){
	       scriptEngineManager = new ScriptEngineManager(); 
	       nashorn = scriptEngineManager.getEngineByName("nashorn"); 
	       engine = scriptEngineManager.getEngineByName("Javascript");  
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t1() throws ScriptException, NoSuchMethodException, FileNotFoundException{
		
		nashorn.eval("function sum(a,b){return a+b;}");
        System.out.println(nashorn.eval("sum(1,2);"));
        
        Invocable invocable = (Invocable) nashorn;
        System.out.println(invocable.invokeFunction("sum",1,3));
        
//        Adder adderaa = (Adder) invocable.getInterface(Adder.class);
//        System.out.println(""+ adderaa.sum(2,3));
        
        
        String s2="function sayHello(){return 'Hello Nashorn!' ;}";
        nashorn.eval(s2);
        System.err.println(nashorn.eval("sayHello()"));
        
        String s3="function f1(a){return a+'test'}";
        nashorn.eval(s3);
        System.out.println(nashorn.eval("f1('test')"));
        
        
        nashorn.eval(new FileReader("e:/sample.js"));
        //var result = calculate(568000000000000000023,13.9);
        Invocable invocable2 = (Invocable) nashorn;
        System.err.println(invocable2.invokeFunction("calculate",56800000,13.9));
        
    //    nashorn.eval(new FileReader("e:/sample2.js"));
     //   System.err.println(invocable2.invokeFunction("sqr",5));
        
      nashorn.eval(new FileReader("e:/sample3.js"));
      Map<String, String>map=(Map<String, String>) nashorn.eval("getMap()");
      Item item=(Item) nashorn.eval("getItem()");
      System.err.println(item);
      System.out.println("----"+map);
      System.err.println(nashorn.eval("getItems()"));
      
      System.err.println(nashorn.eval("getStartTime()"));
      System.err.println(nashorn.eval("isActive()"));
      //getWeight
      System.err.println(nashorn.eval("getWeight()"));
      nashorn.eval("setWeight(200)");
      System.err.println(nashorn.eval("getWeight()"));
      
    //  System.out.println(ScriptUtils.convert(engine.eval("[1,2]"), int[].class));
     Object obj=  nashorn.eval("getD()");
      System.err.println(obj.getClass().getName());  
      ScriptObjectMirror so = (ScriptObjectMirror)obj;  
     System.out.println( so.isArray());
    System.out.println(  so.values());
    //  System.out.println(so.get("tom").getClass().getName()); 
    
    System.err.println(nashorn.eval("getList()"));
      
       System.err.println( nashorn.getContext().getBindings(ScriptContext.ENGINE_SCOPE));
       
       
	}
	
	
	
	
	
	@Test
	public void test1() throws FileNotFoundException, ScriptException{
	    String name = "GBtags.com"; 

	      Integer result = null;
	      try {
	         nashorn.eval("print('" + name + "')");
	         result = (Integer) nashorn.eval("10 + 2");   
	         
	         engine.put("msg", "just a test");  
	         //定义类user  
	         String str = "msg += '!!!';var user = {name:'tom',age:23,hobbies:['football','basketball']}; ";  
	         engine.eval(str);  
	        
	         //从上下文引擎中取值  
	         String msg = (String) engine.get("msg");  
	         String name1 = (String) engine.get("name");  
	         //String[] hb = (String[]) engine.get("hb");  
	         System.out.println(msg);  
	        System.out.println(name1);  
	  //     System.out.println(name1 + ":" + hb[0]);  
	       
	      }catch(ScriptException e) {
	         System.out.println("Error executing script: "+ e.getMessage());
	      }
	      System.err.println(result.toString());
	      
	}

	
	@Test
	public void test111(){
		System.out.println(new Date(1490675430459l));
	}
}
