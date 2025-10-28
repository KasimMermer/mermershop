package com.mermershop.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProfileDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
