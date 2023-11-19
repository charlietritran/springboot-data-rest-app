package com.trancha.demo.model;

import javax.persistence.*;
@Entity
@Table(name = "people")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "birsthdate")
	private String birthdate;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "documents")
	private String documents;
	
	public Person() {
	}
	
	public Person(String firstname, String lastname, String birthdate, String gender, String documents) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.gender = gender;
		this.documents = documents;
	}
	public long getId() {
		return id;
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
	
	public String getDocuments() {
		return this.documents;
	}
	public void setDocuments(String documents) {
		this.documents = documents;
	}
	
	
	@Override
	public String toString() {
		return "people [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", gender=" + gender + ", birthdate=" + birthdate + ", documents=" + documents +"]";
	}
}