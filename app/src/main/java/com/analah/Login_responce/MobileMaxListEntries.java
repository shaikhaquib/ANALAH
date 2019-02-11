package com.analah.Login_responce;

import com.google.gson.annotations.SerializedName;

public class MobileMaxListEntries{

	@SerializedName("name")
	private String name;

	@SerializedName("value")
	private Object value;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setValue(Object value){
		this.value = value;
	}

	public Object getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"MobileMaxListEntries{" + 
			"name = '" + name + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}