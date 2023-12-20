package com.example.lab5.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
  private String message;
  private String timeStamp;
}
