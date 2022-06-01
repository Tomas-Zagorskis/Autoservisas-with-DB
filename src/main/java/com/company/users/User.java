package com.company.users;


import javax.persistence.*;

import static com.company.users.Role.REGISTER;

@Entity
@Table(name = "user")
public class User {
	@Id
	private String username;
	private String password;
	@Column(name = "user_role")
	@Convert(converter = RoleConverter.class)
	private Role role;
	private String name;
	private String surname;
	private int age;

	public User() {
	}

	public User(String username, String password, String name, String surname, int age) {

		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.age = age;
		this.role = REGISTER;
	}

	public User(String username, String password, Role role, String name, String surname, int age) {

		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.role = role;
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {

		return name;
	}

	public String getSurname() {

		return surname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	@Override
	public String toString() {

		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", age=" + age +
				'}';
	}

}
