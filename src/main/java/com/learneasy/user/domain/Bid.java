package com.learneasy.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Entity
public class Bid extends BaseEntity{

    @Id
    private String bidId;

    private List<Schedule> schedules;
    private String subjectId;
    private String tutorId;


}
