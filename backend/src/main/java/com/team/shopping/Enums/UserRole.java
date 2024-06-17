package com.team.shopping.Enums;

public enum UserRole {
    USER, SELLER, ADMIN
    //
    ;
    public String getValue(){
        return "ROLE_"+this.name();
    }

}
