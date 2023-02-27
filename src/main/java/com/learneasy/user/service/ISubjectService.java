package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.infrastructure.dto.SubjectDTO;

import java.util.List;

public interface ISubjectService {
    public SubjectDTO createSubject(SubjectDTO bid) throws Exception;

    public SubjectDTO findSubjectBySubjectId(String subjectID);

    public List<SubjectDTO> findSubjectsByStudentId(String studentId) ;

    public SubjectDTO updateSubject( SubjectDTO updatedSubject) throws JsonMappingException ;

    public List<SubjectDTO> findSubjectsStudentIdTagName(String studentId,String tagName) ;


}
