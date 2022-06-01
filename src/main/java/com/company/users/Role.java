package com.company.users;

public enum Role {

	ADMIN("ADMIN"),
	REGISTER("REGISTER"),
	MECHANIC("MECHANIC");

	private final String role;

	Role(String role) {
		this.role = role;
	}

	public static Role fromRole(String role) {
		if (role.equals(ADMIN.role)) {
			return ADMIN;
		} else if (role.equals(REGISTER.role)) {
			return REGISTER;
		} else if (role.equals(MECHANIC.role)) {
			return MECHANIC;
		} else {
			return null;
		}
	}

	public String getRole() {
		return role;
	}
}