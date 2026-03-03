package co.in.sagarkale.linkedin.users_service.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String email;
    private String password;
}
