package com.learneasy.user.infrastructure.mapper;

import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Subject;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.SubjectDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper {

    Subject subjectDTOToSubject(SubjectDTO subjectDTO);
    SubjectDTO subjectToSubjectDTO(Subject subject);

    List<Subject> subjectDTOToSubject(List<SubjectDTO> subjectDTO);
    List<SubjectDTO> subjectsToSubjectDTOs(List<Subject> subject);

}
