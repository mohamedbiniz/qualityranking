package com.dialogy.odb.test.vo.login;

public class User {
	private String name;
	private String email;
	private Profile profile;
	
	
	public User(){
	}
	public User(String name, String email, Profile profile) {
		super();
		this.name = name;
		this.email = email;
		this.profile = profile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public String toString() {
		return name + " - " + email + " - " + profile;
	}
	

}
