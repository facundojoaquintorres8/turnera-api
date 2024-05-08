package com.f8.turnera.util;

import org.springframework.util.StringUtils;

import com.f8.turnera.exception.BadRequestException;

public class EmailValidation {

    public static void validateEmail(String email) {
        var validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-_%";
        String[] parts = email.split("@");
        if (parts.length != 2) {
            throw new BadRequestException("El Correo Electrónico es inválido.");
        }
        String username = parts[0];
        String domain = parts[1];

        Boolean result = true;

        if (domain.startsWith(".") || domain.startsWith("-")
        || domain.startsWith("_") || domain.startsWith("%")
        || domain.endsWith(".") || domain.endsWith("-") || domain.endsWith("_")
        || domain.endsWith("%")
        || StringUtils.containsWhitespace(username) || username.isEmpty()
        || StringUtils.containsWhitespace(domain) || domain.isEmpty()) {
            result = false;
        }

        for (char item : username.toCharArray()) {
            if (validCharacters.indexOf(item) == -1) {
                result = false;
            }
        }

        for (char item : domain.toCharArray()) {
            if (validCharacters.indexOf(item) == -1) {
                result = false;
            }
        }

        if (domain.indexOf(".") == -1) {
            result = false;
        }

        if (!result) {
            throw new BadRequestException("El Correo Electrónico es inválido.");
        }
    }
}
