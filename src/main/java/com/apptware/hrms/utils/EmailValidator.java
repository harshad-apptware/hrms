package com.apptware.hrms.utils;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailValidator {

  private static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@apptware\\.com$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  public static boolean sanitizeEmail(String email) {
    return !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
  }

  public static String normalizeEmail(String email) {
    if (email.isEmpty()) return null;
    return email.trim().toLowerCase();
  }
}
