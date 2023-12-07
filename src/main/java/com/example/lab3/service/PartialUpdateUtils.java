package com.example.lab3.service;

import java.util.function.Consumer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartialUpdateUtils {
  public static void updateIfNotNullAndNotEmpty(String value, Consumer<String> setter) {
    if (value != null && !value.isEmpty()) {
      setter.accept(value);
    }
  }

  public static <T> void updateIfNotNull(T value, Consumer<T> setter) {
    if (value != null) {
      setter.accept(value);
    }
  }
}
