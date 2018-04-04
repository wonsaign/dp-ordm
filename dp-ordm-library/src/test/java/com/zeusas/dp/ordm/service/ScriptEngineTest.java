package com.zeusas.dp.ordm.service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.junit.Test;  

public class ScriptEngineTest {
	public static void main(String[] args) throws Exception {  
	    ScriptEngineManager sem = new ScriptEngineManager();  
	    ScriptEngine engine = sem.getEngineByName("javascript");     //python or jython,   
	  
//	<pre name="code" class="html">   //向上下文中存入变量  
	   engine.put("msg", "just a test");  
	   //定义类user  
	   String str = "msg += '!!!';var user = {name:'tom',age:23,hobbies:['football','basketball']}; ";  
	   engine.eval(str);  
	  
	   //从上下文引擎中取值  
	   String msg = (String) engine.get("msg");  
	   String name = (String) engine.get("name");  
	   String[] hb = (String[]) engine.get("hb");  
	   System.out.println(msg);  
	//   System.out.println(name + ":" + hb[0]);  
	  
	   //定义数学函数  
	   engine.eval("function add (a, b) {c = a + b; return c; }");  
	  
	    //取得调用接口  
	    Invocable jsInvoke = (Invocable) engine;  
	  
	  //定义加法函数  
	  
	  Object result1 = jsInvoke.invokeFunction("add", new Object[] { 20, 5 });  
	  
	  System.out.println(result1);  
	  
	  //调用加法函数,注意参数传递的方法  
	  
	  engine.eval("function run() {print('www.java2s.com');}");  
	  
	  Invocable invokeEngine = (Invocable) engine;  
	  
	  Runnable runner = invokeEngine.getInterface(Runnable.class);  
	  //定义线程运行之  
	  
	  Thread t = new Thread(runner);  
	  
	  t.start();  
	  
	  t.join();  
	  
	  //导入其他java包  
	  
	  String jsCode = "importPackage(java.util); var list = Arrays.asList([\"首钢股份有限公司\",\"硅钢事业部\",\"三作业区\"]);";
	  
	 // var list2 = Arrays.asList(['A', 'B', 'C']);  
	  
	  engine.eval(jsCode);  
	  
	 // List<String> list2 = (List<String>) engine.get("list2");  
	  
	//  for (String val : list2) { System.out.println(val);}  
	  
	  }  
	
	@Test
	public void 测试绑定() throws ScriptException{
	      ScriptEngineManager scriptEngineManager = new ScriptEngineManager(); 
	      ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn"); 
		int valueIn = 10; 
		SimpleBindings simpleBindings = new SimpleBindings(); 
		simpleBindings.put("globalValue", valueIn); 
		nashorn.eval("print (globalValue)", simpleBindings);
		System.err.println(nashorn.eval("1 + 2"));
		List<Integer> list = Arrays.asList(3, 4, 1, 2); 
		list.forEach(new Consumer<Object>() { 
		    @Override 
		    public void accept(Object o) { 
		        System.out.println(o); 
		    } 
		});
		
	}
	  
	}  