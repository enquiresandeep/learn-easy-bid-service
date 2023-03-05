package com.learneasy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class BaseEntity {
    public String  errorMessage;
    public ZonedDateTime createdDate;
    public ZonedDateTime updatedDate;

}
