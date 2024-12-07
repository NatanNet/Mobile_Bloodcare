package com.example.bloodcare.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ApiBase {
    private static ApiBase instance;
    private RequestQueue requestQueue;
    private static final String BASE_URL = "https://bloodcare.my.id/api/"; // Base URL API

    private ApiBase(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized ApiBase getInstance(Context context) {
        if (instance == null) {
            instance = new ApiBase(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        return requestQueue;
    }

    // POST request
    public void postRequest(String endpoint, JSONObject params, final ApiResponseCallback callback) {
        String fullUrl = BASE_URL + endpoint;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                fullUrl,
                params,
                response -> {
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                },
                error -> {
                    if (callback != null) {
                        callback.onError(error.getMessage() != null ? error.getMessage() : "Unknown error");
                    }
                }
        );
        getRequestQueue().add(request);
    }

    // GET request
    public void getRequest(String endpoint, final ApiResponseCallback callback) {
        String fullUrl = BASE_URL + endpoint;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                fullUrl,
                null, // GET tidak memerlukan body
                response -> {
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                },
                error -> {
                    if (callback != null) {
                        callback.onError(error.getMessage() != null ? error.getMessage() : "Unknown error");
                    }
                }
        );
        getRequestQueue().add(request);
    }
    public void putRequest(String endpoint, JSONObject params, final ApiResponseCallback callback) {
        String fullUrl = BASE_URL + endpoint;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                fullUrl,
                params,
                response -> {
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                },
                error -> {
                    if (callback != null) {
                        callback.onError(error.getMessage() != null ? error.getMessage() : "Unknown error");
                    }
                }
        );
        getRequestQueue().add(request);
    }

    // DELETE request
    public void deleteRequest(String endpoint, final ApiResponseCallback callback) {
        String fullUrl = BASE_URL + endpoint;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                fullUrl,
                null, // DELETE biasanya tidak memerlukan body
                response -> {
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                },
                error -> {
                    if (callback != null) {
                        callback.onError(error.getMessage() != null ? error.getMessage() : "Unknown error");
                    }
                }
        );
        getRequestQueue().add(request);
    }
}
