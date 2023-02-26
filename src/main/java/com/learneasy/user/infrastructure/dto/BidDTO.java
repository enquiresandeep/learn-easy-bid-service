package com.learneasy.user.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO  extends  BaseDTO{

    private String bidId;
    private String firstName;
    private String lastName;
    private String bidStatus;
    private List<PhoneDTO> phones;
    private int defaultAddressID;
    private int defaultPaymentProfileId;

}
