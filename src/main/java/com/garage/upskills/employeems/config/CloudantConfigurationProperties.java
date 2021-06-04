package com.garage.upskills.employeems.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Data
@ConfigurationProperties("cloudant")
public class CloudantConfigurationProperties {
    private URL url;
    private String username;
    private String password;
    private String db;
}
