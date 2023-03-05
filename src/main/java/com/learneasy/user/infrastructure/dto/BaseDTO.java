package com.learneasy.user.infrastructure.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BaseDTO {
    public String  errorMessage;
    public ZonedDateTime createdDate;
    public ZonedDateTime updatedDate;
}
