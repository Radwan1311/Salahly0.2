package com.radwan1311.salahly;

public class DatabaseData {
    private String name;
    private String city;
    private String phone;
    private String address;
    private String job;
    private String imageUrl;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;


    public DatabaseData() {

    }


    public DatabaseData(String name, String city, String phone, String address, String job, String imageUrl, String imageUrl2, String imageUrl3, String imageUrl4) {
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.address = address;
        this.job = job;
        this.imageUrl = imageUrl;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;

    }


    public String getImageUrl2() { return imageUrl2; }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() { return imageUrl3; }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
