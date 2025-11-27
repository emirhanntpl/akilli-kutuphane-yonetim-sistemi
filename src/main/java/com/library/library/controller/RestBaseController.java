package com.library.library.controller;



public class RestBaseController {

    public  <T> RootEntity <T> ok(T payload){
        return  RootEntity.ok(payload);
    }
    public  <T> RootEntity <T> ok(String errorMessage){
        return  RootEntity.error(errorMessage);
    }

}
