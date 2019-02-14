package com.analah.DetailsRespoone;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DetailResponse {

	@SerializedName("relationship_list")
	private List<Object> relationshipList;

	@SerializedName("entry_list")
	private List<EntryListItem> entryList;

	public void setRelationshipList(List<Object> relationshipList){
		this.relationshipList = relationshipList;
	}

	public List<Object> getRelationshipList(){
		return relationshipList;
	}

	public void setEntryList(List<EntryListItem> entryList){
		this.entryList = entryList;
	}

	public List<EntryListItem> getEntryList(){
		return entryList;
	}

	@Override
 	public String toString(){
		return 
			"DetailResponse{" +
			"relationship_list = '" + relationshipList + '\'' + 
			",entry_list = '" + entryList + '\'' + 
			"}";
		}
}