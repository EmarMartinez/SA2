package com.bosonit.sa2.application;


import com.bosonit.sa2.Sa2Application;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    String ruta = Sa2Application.ruta;

    private String location = "upload-dir";

    public StorageProperties() {
        if(ruta.equals("")){
            this.setLocation("upload-dir");
        }
        else{
            this.setLocation(this.ruta);
        }

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }

}