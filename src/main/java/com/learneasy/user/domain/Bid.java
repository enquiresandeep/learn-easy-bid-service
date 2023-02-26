package com.learneasy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Entity
public class Bid extends BaseEntity{

    @Id
    private String bidId;

    private String firstName;
    private String lastName;
    private String bidStatus;
    private List<Phone> phones;
    private int defaultAddressID;
    private int defaultPaymentProfileId;

}
