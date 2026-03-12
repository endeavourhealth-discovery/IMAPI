package org.endeavourhealth.imapi.utility;

import jakarta.servlet.http.HttpServletRequest;

public class IpExtractor {
  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0];
    }
    return ip;
  }
}
