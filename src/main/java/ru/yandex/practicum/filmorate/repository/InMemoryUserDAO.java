package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserDAO {
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User create(User user) {
        user.setId(getNextId());
        log.debug("Generated ID for user: {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Added user: {}", user.toString());
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("User is not found with id {}", user.getId());
            throw new NotFoundException("Error: User with id " + user.getId() + " is not found");
        }

        User oldUser = users.get(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setName(user.getName());
        log.debug("Updated user: {}", oldUser.toString());

        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
