package com.learneasy.user.infrastructure.dto;

import com.learneasy.user.domain.ADDRESS_TYPE;
import com.learneasy.user.domain.BaseEntity;
import com.learneasy.user.domain.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO  extends  BaseDTO{
    private String id;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String bidId;
    private String tutorId;
    private String addressType;

}

