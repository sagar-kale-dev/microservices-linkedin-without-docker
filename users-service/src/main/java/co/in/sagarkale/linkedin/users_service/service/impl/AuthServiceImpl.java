package co.in.sagarkale.linkedin.users_service.service.impl;

import co.in.sagarkale.linkedin.users_service.dto.LoginReqDto;
import co.in.sagarkale.linkedin.users_service.dto.SignupReqDto;
import co.in.sagarkale.linkedin.users_service.dto.UserDto;
import co.in.sagarkale.linkedin.users_service.entity.User;
import co.in.sagarkale.linkedin.users_service.exception.BadRequestException;
import co.in.sagarkale.linkedin.users_service.exception.ResourceNotFoundException;
import co.in.sagarkale.linkedin.users_service.repository.UserRepository;
import co.in.sagarkale.linkedin.users_service.service.AuthService;
import co.in.sagarkale.linkedin.users_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signup(SignupReqDto signupRequestDto) {
        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists, cannot signup again.");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginReqDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }

        return jwtService.generateAccessToken(user);
    }
}
