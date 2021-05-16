package com.app.vipsaffinity.models;

public class Undergraduate {
    // class variables
    private long enrollmentNo;
    private int rankCET, rollNo;
    private String firstName, lastName, dob, gender, gmailID;
    Residence residence;

    public Undergraduate() {
        //public default constructor needed for FireStore get save data in the object
    }

    public long getEnrollmentNo() {
        return enrollmentNo;
    }

    public int getRankCET() {
        return rankCET;
    }

    public int getRollNo() {
        return rollNo;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getGmailID() {
        return gmailID;
    }

    public Residence getResidence() {
        return residence;
    }

    public static class Residence {
        // class variables
        int pinCode;
        String area, city, district, houseNo, state;

        Residence() {
            //public default constructor needed for FireStore get save data in the object
        }

        public int getPinCode() {
            return pinCode;
        }

        public String getArea() {
            return area;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getHouseNo() {
            return houseNo;
        }

        public String getState() {
            return state;
        }
    }
}