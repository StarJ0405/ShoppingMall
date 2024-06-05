package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum OsType {
    Window("C:/web/shopping"), Linux("/home/ubuntu/shopping/data")
    //
    ;
    private final String loc;

    OsType(String loc) {
        this.loc = loc;
    }

    public static OsType getOsType() {
        String osName= System.getProperty("os.name").toLowerCase();

        if(osName.contains("window"))  return Window;
        if(osName.contains("linux"))  return Linux;


        return null;
    }
}
