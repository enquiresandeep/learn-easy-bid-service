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
public class Subject extends BaseEntity{
    @Id
    private String subjectId;

    private String studentId;
    private String title;
    private String detail;
    private String comment;
    private List<SubjectTag> subjectTags;
    private List<Schedule> openSchedule;

}
