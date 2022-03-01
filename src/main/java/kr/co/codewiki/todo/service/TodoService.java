package kr.co.codewiki.todo.service;

import kr.co.codewiki.todo.model.TodoEntity;
import kr.co.codewiki.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    // 새 할일 create
    public List<TodoEntity> create(final TodoEntity entity) {

        // Validations
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    // 인증 + 검증
    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    // userid 로 todoEntity find
    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }
}
