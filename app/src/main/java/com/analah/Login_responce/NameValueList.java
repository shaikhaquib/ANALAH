package com.analah.Login_responce;

import com.google.gson.annotations.SerializedName;

public class NameValueList{

	@SerializedName("mobile_max_list_entries")
	private MobileMaxListEntries mobileMaxListEntries;

	@SerializedName("user_currency_name")
	private UserCurrencyName userCurrencyName;

	@SerializedName("user_default_timeformat")
	private UserDefaultTimeformat userDefaultTimeformat;

	@SerializedName("user_is_admin")
	private UserIsAdmin userIsAdmin;

	@SerializedName("user_decimal_seperator")
	private UserDecimalSeperator userDecimalSeperator;

	@SerializedName("user_name")
	private UserName userName;

	@SerializedName("user_currency_id")
	private UserCurrencyId userCurrencyId;

	@SerializedName("mobile_max_subpanel_entries")
	private MobileMaxSubpanelEntries mobileMaxSubpanelEntries;

	@SerializedName("user_default_team_id")
	private UserDefaultTeamId userDefaultTeamId;

	@SerializedName("user_number_seperator")
	private UserNumberSeperator userNumberSeperator;

	@SerializedName("user_id")
	private UserId userId;

	@SerializedName("user_default_dateformat")
	private UserDefaultDateformat userDefaultDateformat;

	@SerializedName("user_language")
	private UserLanguage userLanguage;

	public void setMobileMaxListEntries(MobileMaxListEntries mobileMaxListEntries){
		this.mobileMaxListEntries = mobileMaxListEntries;
	}

	public MobileMaxListEntries getMobileMaxListEntries(){
		return mobileMaxListEntries;
	}

	public void setUserCurrencyName(UserCurrencyName userCurrencyName){
		this.userCurrencyName = userCurrencyName;
	}

	public UserCurrencyName getUserCurrencyName(){
		return userCurrencyName;
	}

	public void setUserDefaultTimeformat(UserDefaultTimeformat userDefaultTimeformat){
		this.userDefaultTimeformat = userDefaultTimeformat;
	}

	public UserDefaultTimeformat getUserDefaultTimeformat(){
		return userDefaultTimeformat;
	}

	public void setUserIsAdmin(UserIsAdmin userIsAdmin){
		this.userIsAdmin = userIsAdmin;
	}

	public UserIsAdmin getUserIsAdmin(){
		return userIsAdmin;
	}

	public void setUserDecimalSeperator(UserDecimalSeperator userDecimalSeperator){
		this.userDecimalSeperator = userDecimalSeperator;
	}

	public UserDecimalSeperator getUserDecimalSeperator(){
		return userDecimalSeperator;
	}

	public void setUserName(UserName userName){
		this.userName = userName;
	}

	public UserName getUserName(){
		return userName;
	}

	public void setUserCurrencyId(UserCurrencyId userCurrencyId){
		this.userCurrencyId = userCurrencyId;
	}

	public UserCurrencyId getUserCurrencyId(){
		return userCurrencyId;
	}

	public void setMobileMaxSubpanelEntries(MobileMaxSubpanelEntries mobileMaxSubpanelEntries){
		this.mobileMaxSubpanelEntries = mobileMaxSubpanelEntries;
	}

	public MobileMaxSubpanelEntries getMobileMaxSubpanelEntries(){
		return mobileMaxSubpanelEntries;
	}

	public void setUserDefaultTeamId(UserDefaultTeamId userDefaultTeamId){
		this.userDefaultTeamId = userDefaultTeamId;
	}

	public UserDefaultTeamId getUserDefaultTeamId(){
		return userDefaultTeamId;
	}

	public void setUserNumberSeperator(UserNumberSeperator userNumberSeperator){
		this.userNumberSeperator = userNumberSeperator;
	}

	public UserNumberSeperator getUserNumberSeperator(){
		return userNumberSeperator;
	}

	public void setUserId(UserId userId){
		this.userId = userId;
	}

	public UserId getUserId(){
		return userId;
	}

	public void setUserDefaultDateformat(UserDefaultDateformat userDefaultDateformat){
		this.userDefaultDateformat = userDefaultDateformat;
	}

	public UserDefaultDateformat getUserDefaultDateformat(){
		return userDefaultDateformat;
	}

	public void setUserLanguage(UserLanguage userLanguage){
		this.userLanguage = userLanguage;
	}

	public UserLanguage getUserLanguage(){
		return userLanguage;
	}

	@Override
 	public String toString(){
		return 
			"NameValueList{" + 
			"mobile_max_list_entries = '" + mobileMaxListEntries + '\'' + 
			",user_currency_name = '" + userCurrencyName + '\'' + 
			",user_default_timeformat = '" + userDefaultTimeformat + '\'' + 
			",user_is_admin = '" + userIsAdmin + '\'' + 
			",user_decimal_seperator = '" + userDecimalSeperator + '\'' + 
			",user_name = '" + userName + '\'' + 
			",user_currency_id = '" + userCurrencyId + '\'' + 
			",mobile_max_subpanel_entries = '" + mobileMaxSubpanelEntries + '\'' + 
			",user_default_team_id = '" + userDefaultTeamId + '\'' + 
			",user_number_seperator = '" + userNumberSeperator + '\'' + 
			",user_id = '" + userId + '\'' + 
			",user_default_dateformat = '" + userDefaultDateformat + '\'' + 
			",user_language = '" + userLanguage + '\'' + 
			"}";
		}
}