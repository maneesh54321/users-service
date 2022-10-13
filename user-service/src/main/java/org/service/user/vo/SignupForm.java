package org.service.user.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class SignupForm {
    private String email;
    private String fullName;
    private String phoneNo;
    private LocalDate dob;
    private String password;

    public void setDob(String dob){
        this.dob = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
