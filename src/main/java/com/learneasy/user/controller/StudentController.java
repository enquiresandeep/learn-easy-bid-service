package com.learneasy.user.controller;

import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.StudentDTO;
import com.learneasy.user.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private final IStudentService _studentService;

    public StudentController(IStudentService studentService){
        this._studentService = studentService;
    }


    @PostMapping("/")
    public ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO student) {
        log.info("StudentService saveStudent new logs "+student.getFirstName());
        try{
            return ResponseEntity.ok( _studentService.createStudent(student));
        }catch(Exception e){
            log.error("StudentService error {}", student.getFirstName());
            StudentDTO errorStudent = new StudentDTO();
            errorStudent.setErrorMessage("Server Error");
            return new ResponseEntity<>(errorStudent, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO student) {
        try {
            StudentDTO updatedStudent = _studentService.updateStudent(student);
            return ResponseEntity.ok(updatedStudent);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            StudentDTO errorStudent = new StudentDTO();
            errorStudent.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorStudent, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            StudentDTO errorStudent = new StudentDTO();
            errorStudent.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorStudent, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public StudentDTO findStudentByStudentId(@PathVariable("id") String studentId){
        log.info("StudentService findStudentBuId "+studentId);
        return _studentService.findStudentByStudentId(studentId);
    }

    @GetMapping("/")
    public List<StudentDTO>  findAll(){
        log.info("findAll ");
        return _studentService.findAll();
    }

    @PostMapping("/createAddress")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO address) {
        log.info("StudentService saveStudent "+address.getStreet());
        try {
            AddressDTO updatedAddress = _studentService.createAddress(address);
            return ResponseEntity.ok(updatedAddress);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            AddressDTO errorAddress = new AddressDTO();
            errorAddress.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorAddress, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/updateAddress")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO address) {
        try {
            AddressDTO updatedAddress = _studentService.updateAddress(address);
            return ResponseEntity.ok(updatedAddress);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            AddressDTO errorAddress = new AddressDTO();
            errorAddress.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorAddress, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/address/{id}")
    public ResponseEntity<List<AddressDTO>> findAddresesByStudentId(@PathVariable("id") String studentId){
        log.info("StudentService findStudentBuId "+studentId);
        return ResponseEntity.ok(_studentService.findAddressesByStudentId(studentId));
    }



}
