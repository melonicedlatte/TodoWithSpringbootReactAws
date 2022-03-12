package kr.co.codewiki.todo.controller;

import kr.co.codewiki.todo.dto.ResponseDTO;
import kr.co.codewiki.todo.dto.UserDTO;
import kr.co.codewiki.todo.model.UserEntity;
import kr.co.codewiki.todo.security.TokenProvider;
import kr.co.codewiki.todo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    // Bean으로 작성해도 됨.
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입 post
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) { // json 데이터를 받아오기 위해 사용하는 어노테이션

        try {
            // 리퀘스트를 이용해 저장할 유저 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .build();

            // 서비스를 이용해 리파지토리에 유저 저장
            UserEntity registeredUser = userService.create(user);

            // 응답(React) 으로 보낼 유저 만들기
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();

            // 유저 정보는 항상 하나이므로 리스트로 만들어야하는 ResponseDTO 를 사용하지 않고 UserDTO 리턴.
            return ResponseEntity.ok(responseUserDTO);

        } catch (Exception e) {
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    // 로그인 post (JWT 사용 전)
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {// json 데이터를 받아오기 위해 사용하는 어노테이션
//
//        // 로그인 할 때는 이메일과 비번만 입력
//        UserEntity user = userService.getByCredentials(
//                userDTO.getEmail(),
//                userDTO.getPassword());
//
//        if(user != null) {
//            // 유저 토큰 생성
//            final UserDTO responseUserDTO = UserDTO.builder()
//                    .email(user.getEmail())
//                    .id(user.getId())
//                    .build();
//            // 유저 정보는 항상 하나이므로 리스트로 만들어야하는 ResponseDTO 를 사용하지 않고 UserDTO 리턴.
//            return ResponseEntity.ok().body(responseUserDTO);
//
//        } else {
//            ResponseDTO responseDTO = ResponseDTO.builder()
//                    .error("Login failed.")
//                    .build();
//            return ResponseEntity
//                    .badRequest()
//                    .body(responseDTO);
//        }
//    }



    // 로그인 post (JWT 사용 후)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {

        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if(user != null) {

            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);

        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

}
