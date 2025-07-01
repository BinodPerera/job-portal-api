package com.example.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/endpoints")   // Endpoint path for showing all endpoints
public class EndpointController {

    @Autowired  // This will help to inject the RequestMappingHandlerMapping bean
    private RequestMappingHandlerMapping handlerMapping;    // create handlerMapping bean. Bean mean is a class that is managed by Spring's IoC container.
    // IoC (Inversion of Control) container is responsible for managing the lifecycle of beans in a Spring application.

    @GetMapping // This is Get method 
    public List<Map<String, String>> getAllEndpoints() {
        List<Map<String, String>> endpoints = new ArrayList<>();

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods(); 
        // getHandlerMethods() returns a map of RequestMappingInfo to HandlerMethod, which contains all the endpoints defined in the application.

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();    // Get the RequestMappingInfo from the entry
            // RequestMappingInfo contains the mapping information for the endpoint, such as the path and HTTP methods.

            Set<String> patterns = mappingInfo.getPatternValues();  // Get the patterns (paths) from the RequestMappingInfo
            // getPatternValues() returns a set of patterns (paths) that the endpoint is mapped to.
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();    // Get the HTTP methods from the RequestMappingInfo
            // getMethodsCondition() returns a condition that contains the HTTP methods for the endpoint, and getMethods() returns a set of those methods.

            String handlerMethod = entry.getValue().getMethod().getName();

            if (patterns != null && methods != null) {
                for (String pattern : patterns) {
                    for (RequestMethod method : methods) {
                        Map<String, String> endpoint = new HashMap<>();
                        endpoint.put("path", pattern);
                        endpoint.put("method", method.name());
                        endpoint.put("handler", handlerMethod);
                        endpoints.add(endpoint);
                    }
                }
            }
        }
        return endpoints;
    }
}
