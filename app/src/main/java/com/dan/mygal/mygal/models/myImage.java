package com.dan.mygal.mygal.models;

import java.io.Serializable;

// Класс для изобраджения и его свойств (ссылки на большое, маленькое и среднее изображение)
public class myImage implements Serializable{
    private String name;
    private String small, medium, large;


    public myImage() {
    }

    public myImage(String name, String small, String medium, String large) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }


}
