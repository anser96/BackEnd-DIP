package com.diplomado.model;

public class ApiResponse<T> {
    private String status; // Estado de la respuesta (e.g., "success", "error")
    private String message; // Mensaje descriptivo de la respuesta
    private T data; // Datos específicos de la respuesta

    // Constructor vacío
    public ApiResponse() {
    }

    // Constructor con parámetros
    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
