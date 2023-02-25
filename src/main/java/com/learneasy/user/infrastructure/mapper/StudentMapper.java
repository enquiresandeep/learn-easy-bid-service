package com.learneasy.user.infrastructure.mapper;

import com.learneasy.user.domain.Student;
import com.learneasy.user.infrastructure.dto.StudentDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentDTO studentToStudentDTO(Student student);
    Student studentDTOToStudent(StudentDTO studentDTO );

     List<StudentDTO> studentToStudentDTOs(List<Student> students);

     List<Student> studentDTOToStudents(List<StudentDTO> studentDTOs );


}
