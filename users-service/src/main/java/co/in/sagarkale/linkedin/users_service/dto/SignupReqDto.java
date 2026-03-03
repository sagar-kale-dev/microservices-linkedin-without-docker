package co.in.sagarkale.linkedin.users_service.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String name;
    private String email;
    private String password;
}
