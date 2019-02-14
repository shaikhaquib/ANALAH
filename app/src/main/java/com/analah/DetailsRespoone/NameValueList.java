package com.analah.DetailsRespoone;

import com.google.gson.annotations.SerializedName;

public class NameValueList{

	@SerializedName("phone_mobile")
	private PhoneMobile phoneMobile;

	@SerializedName("email1")
	private Email1 email1;

	@SerializedName("account_name")
	private AccountName accountName;

	@SerializedName("lead_source")
	private LeadSource leadSource;

	@SerializedName("name")
	private Name name;

	@SerializedName("phone_work")
	private PhoneWork phoneWork;

	@SerializedName("description")
	private Description description;

	@SerializedName("id")
	private Id id;

	@SerializedName("title")
	private Title title;

	@SerializedName("department")
	private Department department;

	@SerializedName("status")
	private Status status;

	public void setPhoneMobile(PhoneMobile phoneMobile){
		this.phoneMobile = phoneMobile;
	}

	public PhoneMobile getPhoneMobile(){
		return phoneMobile;
	}

	public void setEmail1(Email1 email1){
		this.email1 = email1;
	}

	public Email1 getEmail1(){
		return email1;
	}

	public void setAccountName(AccountName accountName){
		this.accountName = accountName;
	}

	public AccountName getAccountName(){
		return accountName;
	}

	public void setLeadSource(LeadSource leadSource){
		this.leadSource = leadSource;
	}

	public LeadSource getLeadSource(){
		return leadSource;
	}

	public void setName(Name name){
		this.name = name;
	}

	public Name getName(){
		return name;
	}

	public void setPhoneWork(PhoneWork phoneWork){
		this.phoneWork = phoneWork;
	}

	public PhoneWork getPhoneWork(){
		return phoneWork;
	}

	public void setDescription(Description description){
		this.description = description;
	}

	public Description getDescription(){
		return description;
	}

	public void setId(Id id){
		this.id = id;
	}

	public Id getId(){
		return id;
	}

	public void setTitle(Title title){
		this.title = title;
	}

	public Title getTitle(){
		return title;
	}

	public void setDepartment(Department department){
		this.department = department;
	}

	public Department getDepartment(){
		return department;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"NameValueList{" + 
			"phone_mobile = '" + phoneMobile + '\'' + 
			",email1 = '" + email1 + '\'' + 
			",account_name = '" + accountName + '\'' + 
			",lead_source = '" + leadSource + '\'' + 
			",name = '" + name + '\'' + 
			",phone_work = '" + phoneWork + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",department = '" + department + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}