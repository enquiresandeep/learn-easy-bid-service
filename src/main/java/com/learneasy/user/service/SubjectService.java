package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Bid;
import com.learneasy.user.domain.Subject;
import com.learneasy.user.infrastructure.AddressRepository;
import com.learneasy.user.infrastructure.BidRepository;
import com.learneasy.user.infrastructure.SubjectRepository;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.infrastructure.dto.SubjectDTO;
import com.learneasy.user.infrastructure.mapper.AddressMapper;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import com.learneasy.user.infrastructure.mapper.PhoneMapper;
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
        Subject subject =  subjectRepository.save(subjectMapper.subjectDTOToSubject(subjectDTO) );
        return subjectMapper.subjectToSubjectDTO(subject);
    }

    @Override
    public SubjectDTO findSubjectBySubjectId(String subjectID) {
        log.info("SubjectService findBidBuId "+subjectID);
        return  subjectMapper.subjectToSubjectDTO(subjectRepository.findById(subjectID).get());
    }

    @Override
    public List<SubjectDTO> findSubjectsByStudentId(String studentId) {
        return subjectMapper.subjectsToSubjectDTOs(subjectRepository.findSubjectsByStudentId(studentId));
    }

    @Override
    public SubjectDTO updateSubject(SubjectDTO updatedSubject) throws JsonMappingException {
        subjectRepository.findById(updatedSubject.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id " + updatedSubject.getSubjectId()));
        Subject subject = subjectRepository.save(subjectMapper.subjectDTOToSubject(updatedSubject));
        return  subjectMapper.subjectToSubjectDTO(subject);
    }
}
