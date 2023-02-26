package com.learneasy.user.domain;


import org.springframework.data.annotation.Id;

import java.util.List;

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
