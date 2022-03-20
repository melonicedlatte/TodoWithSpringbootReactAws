package kr.co.codewiki.todo.controller;


import kr.co.codewiki.todo.dto.ResponseDTO;
import kr.co.codewiki.todo.dto.TodoDTO;
import kr.co.codewiki.todo.model.TodoEntity;
import kr.co.codewiki.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
@Slf4j
public class TodoController {

    @Autowired
    private TodoService service;


//    @GetMapping("/test")
//    public ResponseEntity<?> testTodo() {
//        String str = service.testService(); // 테스트 서비스 사용
//        List<String> list = new ArrayList<>();
//        list.add(str);
//        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
//        return ResponseEntity.ok(response);
//    }

    /*
    @AuthenticationPrincipal String userId
    인증된 사용자의 아이디를 가져오는 어노테이션. 이 어노테이션을 사용하면 인증된 사용자의 아이디가 인자로 전달된다.
    인증된지는 어떻게 아냐?  JwtAuthenticationFilter 클래스에서 userId 를 매개변수로 넣어줬다.
    이렇게 하면 인증된 사용자의 아이디가 매개변수로 전달된다.
    */

    // 새 할일 작성 post
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // (1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문이다.
            entity.setId(null);

            // (3) 임시 유저 아이디를 설정 해 준다. 이 부분은 4장 인증과 인가에서 수정 할 예정이다. 지금은 인증과 인가 기능이 없으므로 한 유저(temporary-user)만 로그인 없이 사용 가능한 어플리케이션인 셈이다
            entity.setUserId(userId);

            // (4) 서비스를 이용해 Todo엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (7) ResponseDTO를 리턴한다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.

            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 전체 할일 리스트 조회 get
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {

        System.out.println("UserID : " + userId);
        // (1) 서비스 메서드의 retrieve메서드를 사용해 Todo리스트를 가져온다
        List<TodoEntity> entities = service.retrieve(userId);

        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (6) 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (7) ResponseDTO를 리턴한다.
        return ResponseEntity.ok(response);
    }

    // 할일 수정
    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        // (1) dto를 entity로 변환한다.
        TodoEntity entity = TodoDTO.toEntity(dto);

        // (2) id를 userId 초기화 한다.
        entity.setUserId(userId);

        // (3) 서비스를 이용해 entity를 업데이트 한다.
        List<TodoEntity> entities = service.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (6) ResponseDTO를 리턴한다.
        return ResponseEntity.ok(response);
    }

    // 할일 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // (1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) id를 userId 초기화 한다.
            entity.setUserId(userId);

            // (3) 서비스를 이용해 entity를 삭제 한다.
            List<TodoEntity> entities = service.delete(entity);

            // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (6) ResponseDTO를 리턴한다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


}
