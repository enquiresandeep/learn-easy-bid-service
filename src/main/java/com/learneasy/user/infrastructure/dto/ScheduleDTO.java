package com.learneasy.user.infrastructure.dto;

import com.learneasy.user.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ScheduleDTO extends BaseDTO {
    private String startDateTime;
    private String endDateTime;
}
