package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

@Valid
public class RsEvent {
    private  String eventName;
    private  String keyWord;
    @Valid
    private  User user;

    public RsEvent(String eventName,String keyWord,User user){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public RsEvent(){
    }

    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
