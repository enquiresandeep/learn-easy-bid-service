package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.domain.Subject;
import com.learneasy.user.infrastructure.SubjectRepository;
import com.learneasy.user.infrastructure.dto.SubjectDTO;
import com.learneasy.user.infrastructure.mapper.SubjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubjectService implements  ISubjectService{

    @Autowired
    private SubjectRepository subjectRepository;


    @Autowired
    private SubjectMapper subjectMapper;


    @Override
    public SubjectDTO createSubject(SubjectDTO subjectDTO) throws Exception {
        log.info("SubjectService saveBid "+subjectDTO.getTitle());
        Subject subjectInput =  subjectMapper.subjectDTOToSubject(subjectDTO);
        Subject subject =  subjectRepository.save( subjectInput);
        return subjectMapper.subjectToSubjectDTO(subject);
    }

    @Override
    public SubjectDTO findSubjectBySubjectId(String subjectID) {
        log.info("SubjectService findBidBuId "+subjectID);
        return  subjectMapper.subjectToSubjectDTO(subjectRepository.findById(subjectID).get());
    }

    @Override
    public List<SubjectDTO> findSubjectsByStudentId(String studentId) {
        return subjectMapper.subjectsToSubjectDTOs(subjectRepository.findByStudentId(studentId));
    }

    @Override
    public SubjectDTO updateSubject(SubjectDTO updatedSubject) throws JsonMappingException {
        subjectRepository.findById(updatedSubject.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + updatedSubject.getSubjectId()));
        Subject subject = subjectRepository.save(subjectMapper.subjectDTOToSubject(updatedSubject));
        return  subjectMapper.subjectToSubjectDTO(subject);
    }

    @Override
    public List<SubjectDTO> findSubjectsStudentIdTagName(String studentId, String tagName) {
        return subjectMapper.subjectsToSubjectDTOs(subjectRepository.findSubjectsByTagNameAndStudentId(tagName,studentId));

    }
}
