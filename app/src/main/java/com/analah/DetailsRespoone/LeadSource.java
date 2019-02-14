package com.analah.DetailsRespoone;

import com.google.gson.annotations.SerializedName;

public class LeadSource{

	@SerializedName("name")
	private String name;

	@SerializedName("value")
	private String value;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"LeadSource{" + 
			"name = '" + name + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}