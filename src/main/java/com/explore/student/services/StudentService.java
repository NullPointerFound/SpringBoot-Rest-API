package com.explore.student.services;

import com.explore.student.entities.StudentEntity;
import com.explore.student.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public ResponseEntity addStudent(StudentEntity student){
        Optional<StudentEntity> entity = studentRepository.findStudentByEmail(student.getEmail());
        if(!entity.isPresent()){
           StudentEntity s =  studentRepository.save(student);
            return ResponseEntity.ok(s);
        } else {
            return ResponseEntity.badRequest()
                    .header("Custom-Header", "foo")
                    .body("The email is already taken");
        }
    }

    public String deleteStudent(Long studentId){
        boolean exists = studentRepository.existsById(studentId);
        if(exists) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalArgumentException("Student doest not exist with ID: " + studentId);
        }

        return "Student with ID "+studentId+" has been deleted successfully";
    }

    public List<StudentEntity> getStudents(){

        return studentRepository.findAll();
    }

    public Optional<StudentEntity> getStudentByEmail(String email){

        Optional<StudentEntity> entity = studentRepository.findStudentByEmail(email);
        return entity;
    }


    public StudentEntity getStudent(Long studentId){
        Optional<StudentEntity> entity = studentRepository.findStudentById(studentId);
        if(!entity.isPresent()){
            throw new IllegalArgumentException("Student not found "+ studentId);
        }

        return entity.get();
    }


    @Transactional
    public StudentEntity updateStudent(Long id, String name, String email){
        Optional<StudentEntity> studentOptional = studentRepository.findStudentById(id);
        StudentEntity student;

        if(studentOptional.isPresent()){
            student = studentOptional.get();
            student.setEmail(email);
            student.setName(name);
            System.out.println("Student saved successfully " + student);
        } else {
            throw new IllegalArgumentException("Student not found with id: "+ id);
        }

        return student;
    }
}
