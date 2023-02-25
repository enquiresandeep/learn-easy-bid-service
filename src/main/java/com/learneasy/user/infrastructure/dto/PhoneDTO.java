package com.learneasy.user.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO extends  BaseDTO{
    private  String countryCode;
    private  String areaCode;
    private  String phoneNumber;
    private  String type;

}
