package com.example.ssar.dto;

public interface OAuth2Response {

    String getProvider();
    
    String getProviderId();

    String getEmail();

    String getName();
    
    String getNickName();
}