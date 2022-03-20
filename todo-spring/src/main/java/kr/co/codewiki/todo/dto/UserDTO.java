package kr.co.codewiki.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    // React 페이지에서 받아올 데이터들 (회원 가입 시?)

    private String token;
    private String email;
    private String username;
    private String password;
    private String id;

}
