package com.glebzapara.nexor.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(name = "username", nullable = false)
    private String userName;

    @NotNull
    @Size(max = 255)
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @NotNull
    @Email
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Size(max = 16)
    @Pattern(regexp = "^\\+[0-9]{9,15}$")
    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "image")
    private String image;

    @Column(name = "last_seen")
    private ZonedDateTime lastSeen;

    @NotNull
    @Size(max = 20)
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats = new ArrayList<>();
}
