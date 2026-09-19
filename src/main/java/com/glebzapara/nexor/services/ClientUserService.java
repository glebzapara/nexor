package com.glebzapara.nexor.services;

import com.glebzapara.nexor.models.Chat;
import com.glebzapara.nexor.models.User;
import com.glebzapara.nexor.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientUserService {
    UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public ClientUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User findByUserName(String name) {
        return userRepository.findByUserName(name).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void registerUser(User user /*Integer groupId*/) throws Exception {
//        Group studentGroup = groupRepository.findById(groupId)
//                .orElseThrow(() -> new Exception("Group not found"));
//
//        student.setGroup(studentGroup);
//
//        if (student.getName() == null || student.getName().trim().isEmpty()) {
//            throw new Exception("Name cannot be null or empty");
//        }
//
//        if (student.getSurname() == null || student.getSurname().trim().isEmpty()) {
//            throw new Exception("Surname cannot be null or empty");
//        }
//
//        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
//            throw new Exception("Email cannot be null or empty");
//        }
//
//        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
//            throw new Exception("Password cannot be null or empty");
//        }
//
//        if (studentGroup.getCourse() == null
//                || studentGroup.getCourse() < 1
//                || studentGroup.getCourse() > 6) {
//            throw new Exception("Course must be between 1 and 6");
//        }
//
//        if (studentGroup.getSpeciality() == null || studentGroup.getSpeciality().trim().isEmpty()) {
//            throw new Exception("Speciality cannot be null or empty");
//        }
//
//        if (student.getPhoneNumber() == null || student.getPhoneNumber().trim().isEmpty()) {
//            throw new Exception("Phone number cannot be null or empty");
//        }
//
//        student.setPassword(passwordEncoder.encode(student.getPassword()));
//        student.setLastSeen(ZonedDateTime.now(ZoneId.of("Europe/Kyiv")));
//
//        if (student.getId() != null) {
//            Student existingStudent = studentRepository.findById(student.getId())
//                    .orElseThrow(() -> new Exception("Student not found"));
//
//            student.setImage(existingStudent.getImage());
//        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLastSeen(ZonedDateTime.now(ZoneId.of("Europe/Kiev")));

        userRepository.save(user);
    }
}
