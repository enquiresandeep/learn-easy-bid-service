package com.learneasy.user.infrastructure.dto;

import com.learneasy.user.domain.BaseEntity;
import com.learneasy.user.domain.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO  extends  BaseDTO{

    private String studentId;
    private String firstName;
    private String lastName;
    private String studentStatus;
    private List<PhoneDTO> phones;
    private int defaultAddressID;
    private int defaultPaymentProfileId;

}
