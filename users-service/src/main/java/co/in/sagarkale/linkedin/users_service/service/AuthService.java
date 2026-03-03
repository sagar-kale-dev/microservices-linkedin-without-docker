package co.in.sagarkale.linkedin.users_service.service;

import co.in.sagarkale.linkedin.users_service.dto.LoginReqDto;
import co.in.sagarkale.linkedin.users_service.dto.SignupReqDto;
import co.in.sagarkale.linkedin.users_service.dto.UserDto;

public interface AuthService {
    UserDto signup(SignupReqDto signupReqDto);
    String login(LoginReqDto loginReqDto);
}
