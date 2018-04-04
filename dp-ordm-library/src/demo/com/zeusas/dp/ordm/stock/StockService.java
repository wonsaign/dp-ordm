package com.zeusas.dp.ordm.stock;

import java.util.List;

import com.alibaba.fastjson.JSON;

import io.baratine.web.Get;
import io.baratine.web.RequestWeb;
import io.baratine.web.Web;

public class StockService {
	public static void main(String []args){
		Web.property("json.jackson.enabled", "true");
		Web.property("server.port", "8086");
		Web.get("/stockservice.do").to(req->req.ok("Hello, world"));
		Web.go(args);
		Web.include(StockService.class);
		Web.go(args);
	}

}

