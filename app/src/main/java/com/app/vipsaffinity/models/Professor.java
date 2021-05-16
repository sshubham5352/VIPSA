package com.app.vipsaffinity.models;

import java.io.Serializable;

public class Professor implements Serializable {
    // class variables
    String name, bio, department, department_short, image_url;


    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getDepartment() {
        return department;
    }

    public String getDepartment_short() {
        return department_short;
    }

    public String getImage_url() {
        return image_url;
    }
}
