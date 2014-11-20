/*
 * Copyright (C) 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ridesharing.network;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

/**
 * @Package com.ridesharing.network
 * @Author wensheng
 * @Date 2014/11/2.
 */
public class RestHelper<I, R> {
    private String url;
    private I input;
    private ParameterizedTypeReference<R> output;
    private static String cookie;

    public RestHelper(String url, I input, ParameterizedTypeReference<R> output){
        this.url = url;
        this.input = input;
        this.output = output;
    }

    public R execute(){
        // Set the Content-Type header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application","json"));
        if(cookie != null){
            requestHeaders.add("Cookie", cookie);
        }
        HttpEntity<String> requestEntity = new HttpEntity<String>(new Gson().toJson(input), requestHeaders);
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        //MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //restTemplate.getMessageConverters().add(5, jackson2HttpMessageConverter);
        // Add the Gson message converter
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response from JSON to an array of Events
        ResponseEntity<R> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, output );
        Log.v("com.ridesharing: URL:" , url);
        Log.v("com.ridesharing: Post: ", requestEntity.getBody());
        List<String> cookieList = responseEntity.getHeaders().get("Set-Cookie");
        if(cookieList != null && cookieList.size() > 0) {
            cookie = responseEntity.getHeaders().get("Set-Cookie").get(0);
            Log.v("com.ride.sharing: cookie",cookie);
        }
        return responseEntity.getBody();
    }
}
