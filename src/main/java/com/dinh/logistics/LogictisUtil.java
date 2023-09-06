package com.dinh.logistics;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class LogictisUtil {
    public static String getBaseUrl() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
            return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
            // build URL from request
        } else {
            // fallback logic if request won't work...
            return "Nothing";
        }
    }

    public static String formatMoney(String money, String unit) {
        String un = unit != null ? unit : "VND";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator((un.equals("VND") || un.equals("VNĐ")) ? ',' : '.');
        symbols.setGroupingSeparator((un.equals("VND") || un.equals("VNĐ")) ? ',' : '.');

        DecimalFormat myFormatter = new DecimalFormat("#,###", symbols);

        String s = "";
        try {
            s = myFormatter.format(Double.parseDouble(money != null ? money : "0"));
        } catch (Exception ignore) {
        }

        return s + " " + un;
    }
}
