package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDAO;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserDAO implements UserDAO {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Collection<Long>> friends = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        log.debug("User/GetAll");
        return users.values();
    }

    @Override
    public User getById(long id) {
        log.debug("User/GetById: id {}", id);
        checkUsers(id);

        return users.get(id);
    }

    @Override
    public User create(User user) {
        log.debug("User/Create");
        user.setId(getNextId());
        log.debug("Generated ID for user: {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Added user: {}", user.toString());
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("User/Update");

        checkUsers(user.getId());

        User oldUser = users.get(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setName(user.getName());
        log.debug("Updated user: {}", oldUser.toString());

        return oldUser;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.debug("User/AddFriend ids: {}, {}", userId, friendId);

        checkUsers(userId, friendId);

        if (!friends.containsKey(userId)) {
            friends.put(userId, new HashSet<>());
        }

        if (friends.get(userId).contains(friendId)) {
            log.debug("User with id {} already added friend with id {}", userId, friendId);
            throw new ValidationException("Error: User already have friend with id " + friendId);
        }

        friends.get(userId).add(friendId);

        if (!friends.containsKey(friendId)) {
            friends.put(friendId, new HashSet<>());
        }

        friends.get(friendId).add(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        log.debug("User/removeFriend ids: {}, {}", userId, friendId);
        checkUsers(userId, friendId);

        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
        }

        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(userId);
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        log.debug("User/getFriend id: {}", id);
        checkUsers(id);

        if (!friends.containsKey(id)) {
            return new HashSet<>();
        }

        return friends.get(id).stream()
                .map(users::get)
                .toList();
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    // Util method for check users
    @Override
    public void checkUsers(long... ids) {
        String notFound = Arrays.stream(ids)
                .filter(id -> !users.containsKey(id))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        if (!notFound.isBlank()) {
            log.debug("User with ID {} is not found", notFound);
            throw new NotFoundException("User with ID " + notFound + " is not found");
        }
    }
}
