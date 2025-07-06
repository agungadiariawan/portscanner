package com.agungadiariawan.portscanner.helpers;

public class ValidasiForm {
    public static String sanitizeIpInput(String raw) {
        String cleaned = raw.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) return "";

        String[] parts = cleaned.split("\\.");
        if (parts.length != 4) return "";

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) return "";
            } catch (NumberFormatException e) {
                return "";
            }
        }

        return cleaned;
    }

    public static Integer sanitizePortInput(String raw) {
        String cleaned = raw.replaceAll("[^0-9]", "");
        if (cleaned.isEmpty()) return null;

        try {
            int port = Integer.parseInt(cleaned);
            if (port < 1 || port > 65535) return null;
            return port;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
