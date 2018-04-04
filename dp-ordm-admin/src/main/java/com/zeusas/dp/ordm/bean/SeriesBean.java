package com.zeusas.dp.ordm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;

import com.zeusas.common.entity.Dictionary;

public class SeriesBean {
	
	private Dictionary dictionary;
	
	private String did;
	private String hardCode;
	private String name;
	private String url;
	private String haveimg;
	private String active;
	private String lastUpdate;
	
	public SeriesBean() {
	}

	public SeriesBean(Dictionary dictionary) {
		this.dictionary = dictionary;
		this.did=dictionary.getDid()==null?"":dictionary.getDid();
		this.hardCode = dictionary.getHardCode()==null?"":dictionary.getHardCode();
		this.name = dictionary.getName()==null?"":dictionary.getName();
		this.url = dictionary.getUrl()==null?"":dictionary.getUrl();
		this.haveimg= this.url.isEmpty()?"无":"有";
		this.active = dictionary.isActive()?"启用":"禁用";
		if(dictionary.getLastUpdate()!=null){
			Date dt=new Date(dictionary.getLastUpdate());
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.lastUpdate=dateFormat.format(dt);
		}else{
			this.lastUpdate="";
		}
	}

	public String getDid() {
		return did==null?"":did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getHardCode() {
		return hardCode==null?"":hardCode;
	}

	public void setHardCode(String hardCode) {
		this.hardCode = hardCode;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url==null?"":url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActive() {
		return active==null?"":active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getLastUpdate() {
		return lastUpdate==null?"":lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getHaveimg() {
		return haveimg==null?"":haveimg;
	}

	public void setHaveimg(String haveimg) {
		this.haveimg = haveimg;
	}
	
}
