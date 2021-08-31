package com.app.vipsaffinity.models;

public class Undergraduate {
    // class variables
    private long enrollmentNo, phoneNo;
    private int rankCET, rollNo;
    private boolean isVerified;
    private String firstName, lastName, fatherName, dob, gender, gmailID, profilePic;
    Residence residence;

    public Undergraduate() {
        //public default constructor needed for FireStore get save data in the object
    }

    public long getEnrollmentNo() {
        return enrollmentNo;
    }

    public long getPhoneNo() {
        return phoneNo;
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

    public String getFatherName() {
        return fatherName;
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

    public boolean isVerified() {
        return isVerified;
    }

    public String getProfilePic() {
        return profilePic;
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

        public String getAddress() {
            try {
                return houseNo + ", " + area + city + ", " + district + ", " + pinCode;
            }catch (Exception e){
                return "N/A";
            }
        }
    }
}