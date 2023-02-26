package com.learneasy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Entity
public class Address extends BaseEntity{
    @Id
    private String id;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String bidId;
    private String tutorId;
    private ADDRESS_TYPE addressType;

}

