package com.trancha.demo.model;

import javax.persistence.*;

import org.springframework.web.multipart.MultipartFile;

public class PersonFormWrapper {


	private String firstname;

	private String lastname;
	
	private String birthdate;

	private String gender;

	private MultipartFile  [] documents;
	
	public PersonFormWrapper() {
	}
	
	public PersonFormWrapper(String firstname, String lastname, String birthdate, String gender, MultipartFile  [] documents) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.gender = gender;
		this.documents = documents;
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	
	public String getGender() {
		return this.gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public MultipartFile  [] getDocuments() {
		return this.documents;
	}
	public void setDocuments(MultipartFile  [] documents) {
		this.documents = documents;
	}
	
	
	@Override
	public String toString() {
		return "people [firstname=" + firstname + ", lastname=" + lastname + ", gender=" + gender + ", birthdate=" + birthdate + ", documents=" + documents.toString() +"]";
	}
}