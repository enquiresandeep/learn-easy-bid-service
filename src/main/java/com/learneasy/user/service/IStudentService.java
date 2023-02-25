package com.learneasy.user.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Student;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.StudentDTO;

import java.util.List;

public interface IStudentService {
    public StudentDTO createStudent(StudentDTO student) throws Exception;

    public StudentDTO findStudentByStudentId(String studentId);

    public StudentDTO updateStudent(StudentDTO updatedStudent) throws JsonMappingException ;
    public List<StudentDTO> findAll();

    public AddressDTO createAddress(AddressDTO address) throws JsonMappingException;

    public List<AddressDTO> findAddressesByStudentId(String studentId) ;

    public AddressDTO updateAddress( AddressDTO updatedAddress) throws JsonMappingException ;
}
