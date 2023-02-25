package com.learneasy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Entity
public class Student extends BaseEntity{

    @Id
    private String studentId;

    private String firstName;
    private String lastName;
    private String studentStatus;
    private List<Phone> phones;
    private int defaultAddressID;
    private int defaultPaymentProfileId;

}
