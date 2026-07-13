package com.festi_ci.parti_ci.utils;

import java.security.SecureRandom;

public class StringUtils {
    public static String slugify(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special chars ( [^...] returns all characters NOT in the set)
                .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                .replaceAll("-+", "-")           // Replace multiple hyphens with single
                .replaceAll("^-|-$", "");         // Trim leading/trailing hyphens
    }

    public static String generateEventKey() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // No I,1,O,0
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return "evt-" + sb.toString(); // e.g., "evt-K7X3M9"
    }
}