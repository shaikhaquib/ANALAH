package com.analah.DetailsRespoone;

import com.google.gson.annotations.SerializedName;

public class EntryListItem{

	@SerializedName("name_value_list")
	private NameValueList nameValueList;

	@SerializedName("id")
	private String id;

	@SerializedName("module_name")
	private String moduleName;

	public void setNameValueList(NameValueList nameValueList){
		this.nameValueList = nameValueList;
	}

	public NameValueList getNameValueList(){
		return nameValueList;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setModuleName(String moduleName){
		this.moduleName = moduleName;
	}

	public String getModuleName(){
		return moduleName;
	}

	@Override
 	public String toString(){
		return 
			"EntryListItem{" + 
			"name_value_list = '" + nameValueList + '\'' + 
			",id = '" + id + '\'' + 
			",module_name = '" + moduleName + '\'' + 
			"}";
		}
}