package com.boa.kyc.zuul.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Configuration
public class ZuulConfiguration {
	@Autowired
    private static DiscoveryClient discoveryClient;
	@Bean
	public ZuulFilter apiPrefixStrip(RouteLocator routeLocator) {
        return new ZuulFilter() {

            @Override
            public String filterType() {
                return "pre";
            }

            @Override
            public int filterOrder() {
                return 0;
            }

            @Override
            public boolean shouldFilter() {
                RequestContext context = RequestContext.getCurrentContext();
                return context.getRequest().getRequestURI().startsWith("/test");
            }

            @Override
            public Object run() {
            	  System.out.println("Route Created"); 
                RequestContext context = RequestContext.getCurrentContext();
                String path = context.getRequest().getRequestURI();
                List<String> serviceIds = discoveryClient.getServices();
           	 
    	        if (serviceIds == null || serviceIds.isEmpty()) {
    	            return "No services found!";
    	        }
                ZuulProperties zuulProperties=new ZuulProperties();
                Map map =new HashMap();
                ZuulRoute route=new ZuulRoute();
                route.setId("test");
                route.setPath("/test/**");
                route.setServiceId(serviceIds.get(0));
                map.put("test", route);
                
                zuulProperties.setRoutes(map);
                System.out.println("Route Created");   
                
                return null;
            }
        };
	}
}