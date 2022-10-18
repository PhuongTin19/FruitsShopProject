package com.tin.custom;

import javax.servlet.http.HttpServletRequest;

public class Utility {
	
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
