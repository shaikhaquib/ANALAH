package com.analah.Login_responce;

import com.google.gson.annotations.SerializedName;

public class UserIsAdmin{

	@SerializedName("name")
	private String name;

	@SerializedName("value")
	private boolean value;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setValue(boolean value){
		this.value = value;
	}

	public boolean isValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"UserIsAdmin{" + 
			"name = '" + name + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}