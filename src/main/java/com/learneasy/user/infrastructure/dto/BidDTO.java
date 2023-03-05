package com.learneasy.user.infrastructure.dto;

import com.learneasy.user.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO  extends  BaseDTO{

    private String bidId;
    private List<Schedule> schedules;
    private String subjectId;
    private String tutorId;

}
