package com.learneasy.user.controller;

import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.infrastructure.dto.SubjectDTO;
import com.learneasy.user.service.IBidService;
import com.learneasy.user.service.ISubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private final ISubjectService _subjectService;

    public SubjectController(ISubjectService subjectService){
        this._subjectService = subjectService;
    }


    @PostMapping("/")
    public ResponseEntity<SubjectDTO> saveSubject(@RequestBody SubjectDTO subject) {
        log.info("SubjectService save Subject new logs "+subject.getTitle());
        try{
            return ResponseEntity.ok( _subjectService.createSubject(subject));
        }catch(Exception e){
            e.printStackTrace();
            log.error("BidService error {}", subject.getTitle());
            SubjectDTO errorSubject = new SubjectDTO();
            errorSubject.setErrorMessage("Server Error");
            return new ResponseEntity<>(errorSubject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<SubjectDTO> updateSubject(@RequestBody SubjectDTO subject) {
        try {
            SubjectDTO updatedSubject = _subjectService.updateSubject(subject);
            return ResponseEntity.ok(updatedSubject);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            SubjectDTO errorSubject = new SubjectDTO();
            errorSubject.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorSubject, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            SubjectDTO errorSubject = new SubjectDTO();
            errorSubject.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorSubject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public SubjectDTO findSubjectBySubjectId(@PathVariable("id") String subjectId){
        log.info("SubjectService findSubjectBySubjectId "+subjectId);
        return _subjectService.findSubjectBySubjectId(subjectId);
    }

    @GetMapping("/subject/{studentId}")
    public ResponseEntity<List<SubjectDTO>> findStudentsByStudentId(@PathVariable("id") String studentId){
        log.info("SubjectService findStudentsByStudentId "+studentId);
        return ResponseEntity.ok(_subjectService.findSubjectsByStudentId(studentId));
    }


}
