package edu.uci.ics.android;

public class Stars {
	private int id;
	private String first_name;
	private String last_name;
	private String dob;
	private String pic_url;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	
	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getPicUrl() {
		return pic_url;
	}
	
	public void setPicUrl(String pic_url) {
		this.pic_url = pic_url;
	}
	
	//will be used by arrayadapter in listview
	@Override
	public String toString() {
		return first_name+" "+last_name;
	}
}
