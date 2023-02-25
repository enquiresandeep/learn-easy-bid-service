package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Student;
import com.learneasy.user.infrastructure.AddressRepository;
import com.learneasy.user.infrastructure.StudentRepository;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.StudentDTO;
import com.learneasy.user.infrastructure.mapper.AddressMapper;
import com.learneasy.user.infrastructure.mapper.PhoneMapper;
import com.learneasy.user.infrastructure.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService implements  IStudentService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
     private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    public StudentDTO createStudent(StudentDTO studentDTO) throws Exception{
        log.info("StudentService saveStudent "+studentDTO.getFirstName());
        Student student =  studentRepository.save(studentMapper.studentDTOToStudent(studentDTO) );
        return studentMapper.studentToStudentDTO(student);
    }

    public StudentDTO findStudentByStudentId(String studentId){
        log.info("StudentService findStudentBuId "+studentId);
        return  studentMapper.studentToStudentDTO(studentRepository.findById(studentId).get());
    }

    public StudentDTO updateStudent(StudentDTO updatedStudent) throws JsonMappingException {
         studentRepository.findById(updatedStudent.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id " + updatedStudent.getStudentId()));
        Student student = studentRepository.save(studentMapper.studentDTOToStudent(updatedStudent));
        return  studentMapper.studentToStudentDTO(student);
    }

    public List<StudentDTO> findAll(){
        return studentMapper.studentToStudentDTOs(studentRepository.findAll());
    }

    public AddressDTO createAddress(AddressDTO addressDTO) throws JsonMappingException{
        log.info("StudentService createAddress "+addressDTO.getStreet());
        String studentId = addressDTO.getStudentId();
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + studentId));
        Address address = addressRepository.save(addressMapper.addressDTOToAddress(addressDTO));
        return  addressMapper.addressToAddressDTO(address);
    }

    public List<AddressDTO> findAddressesByStudentId(String studentId) {
        return  addressMapper.addressToAddressDTOs(addressRepository.findByStudentId(studentId));
    }

    public AddressDTO updateAddress( AddressDTO updatedAddress) throws JsonMappingException {
         addressRepository.findById(updatedAddress.getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id " + updatedAddress.getId()));

        Address address = addressRepository.save( addressMapper.addressDTOToAddress(updatedAddress));

        return addressMapper.addressToAddressDTO(address);
    }


}
