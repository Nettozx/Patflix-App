package edu.uci.ics.android;

public class Movies {
	private int id;
	private String title;
	private int year;
	private String director;
	private String banner_url;
	private String trailer_url;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getBannerUrl() {
		return banner_url;
	}
	
	public void setBannerUrl(String banner_url) {
		this.banner_url = banner_url;
	}
	
	public String getTrailerUrl() {
		return trailer_url;
	}
	
	public void setTrailerUrl(String trailer_url) {
		this.trailer_url = trailer_url;
	}
}
