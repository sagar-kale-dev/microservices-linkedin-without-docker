package co.in.sagarkale.linkedin.users_service.controller;

import co.in.sagarkale.linkedin.users_service.dto.LoginReqDto;
import co.in.sagarkale.linkedin.users_service.dto.LoginRespDto;
import co.in.sagarkale.linkedin.users_service.dto.SignupReqDto;
import co.in.sagarkale.linkedin.users_service.dto.UserDto;
import co.in.sagarkale.linkedin.users_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupReqDto signupReqDto){
        UserDto userDto = authService.signup(signupReqDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespDto> login(@RequestBody LoginReqDto loginReqDto){
        String token = authService.login(loginReqDto);
        LoginRespDto loginRespDto = new LoginRespDto();
        loginRespDto.setToken(token);
        return ResponseEntity.ok(loginRespDto);
    }

}
