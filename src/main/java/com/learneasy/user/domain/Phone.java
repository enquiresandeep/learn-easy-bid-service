package com.learneasy.user.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@ValueObject
public class Phone {
    private  String countryCode;
    private  String areaCode;
    private  String phoneNumber;
    private  String type;

}
