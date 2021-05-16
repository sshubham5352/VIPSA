package com.app.vipsaffinity.models;

import java.io.Serializable;

public class FacultyMember implements Serializable {
    // class variables
    String name, proficiency, experience, bio, department_short, department, image_url, bg_image,
            email_id, phone_number, color_theme;

    public String getDepartment_short() {
        return department_short;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getBg_image() {
        return bg_image;
    }

    public String getColor_theme() {
        return color_theme;
    }

    public String getName() {
        return name;
    }

    public String getProficiency() {
        return proficiency;
    }

    public String getExperience() {
        return experience;
    }

    public String getBio() {
        return bio;
    }

    public String getDepartment() {
        return department;
    }

    public String getImage_url() {
        return image_url;
    }
}
