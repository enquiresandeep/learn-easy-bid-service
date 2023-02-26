package com.learneasy.user.infrastructure.dto;

import com.learneasy.user.domain.Schedule;
import com.learneasy.user.domain.SubjectTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO extends  BaseDTO{

    private String subjectId;

    private String studentId;
    private String title;
    private String detail;
    private String comment;
    private List<SubjectTag > subjectTags;
    private List<Schedule> openSchedule;

}
