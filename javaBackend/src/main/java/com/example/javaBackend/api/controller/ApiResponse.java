package com.example.javaBackend.api.controller;


public class ApiResponse {
    private String status;  // success or error
    private String message;
    private Object data;
    private int statusCode;

    // Constructors, Getters and Setters
    public ApiResponse(String status, String message,Object data, int statusCode){
        this.status =  status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
    public int getStatusCode() { return statusCode; }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse("success", message, data,200);
    }

    public static ApiResponse error(String message,int statusCode) {
        return new ApiResponse("error", message, null,statusCode);
    }

}
