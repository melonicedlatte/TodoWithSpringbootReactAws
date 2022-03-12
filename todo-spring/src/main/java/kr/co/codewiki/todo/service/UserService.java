package kr.co.codewiki.todo.service;

import kr.co.codewiki.todo.model.UserEntity;
import kr.co.codewiki.todo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 유저 생성
    public UserEntity create(final UserEntity userEntity) {

        // 사용자 정보가 없을 때 throw 할 exception
        if(userEntity == null || userEntity.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }

        // 가입 시 입력한 email 을 수정할 수 없게 final 로 지정
        final String email = userEntity.getEmail();

        // 이미 있는 email 일 때 throw 할 exception
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    // 로그인 시 인증에 사용할 메서드
    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {

        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인 (같으면 return)
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }

        return null;
    }

    /*
    matches
    비번을 db 의 값과 비교하지 않고, Salt 로 인코딩된 비번과 비교하는 메서드

    BCryptPasswordEncoder 는 같은 값을 인코딩하더라도 매번 결과가 다르다. => 이런 의미 없는 값을 Salt 라고 한다.
    (Salt 를 붙여서 인코딩하는 것을 Salting 이라고 한다.)
    따라서 다시 인코딩해서 db에 저장된 값이랑 비교해도 값은 틀리다.

    matches 는 BCryptPasswordEncoder 로 인코딩한 값과 비교하는 메서드 (Salt 를 고려해서 값을 비교해줌)
    */

}
