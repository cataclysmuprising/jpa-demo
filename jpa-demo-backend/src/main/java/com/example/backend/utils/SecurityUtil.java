package com.example.backend.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class SecurityUtil {

	private SecurityUtil() {
	}

	public static String getClientIp(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	public static String getOperatingSystem(HttpServletRequest request) {
		String os = "";
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.toLowerCase().contains("windows")) {
			os = "Windows";
		}
		else if (userAgent.toLowerCase().contains("mac")) {
			os = "Mac";
		}
		else if (userAgent.toLowerCase().contains("x11")) {
			os = "Unix";
		}
		else if (userAgent.toLowerCase().contains("android")) {
			os = "Android";
		}
		else if (userAgent.toLowerCase().contains("iphone")) {
			os = "IPhone";
		}
		else {
			os = "UnKnown, More-Info: " + userAgent;
		}
		return os;
	}

	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String user = userAgent.toLowerCase();

		String browser = "";
		if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		}
		else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		}
		else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera")) {
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			}
			else if (user.contains("opr")) {
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
			}
		}
		else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		}
		else if (user.contains("mozilla/7.0") || user.contains("netscape6") || user.contains("mozilla/4.7") || user.contains("mozilla/4.78") || user.contains("mozilla/4.08") || user.contains("mozilla/3")) {
			browser = "Netscape-?";
		}
		else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		}
		else if (user.contains("rv")) {
			browser = "IE";
		}
		else {
			browser = "UnKnown, More-Info: " + userAgent;
		}
		return browser;
	}
}
