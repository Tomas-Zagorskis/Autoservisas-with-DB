package com.company.users;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return (role != null)? role.getRole() : null;
    }


    @Override
    public Role convertToEntityAttribute(String role) {
        return (role != null)? Role.fromRole(role) : null;
    }
}