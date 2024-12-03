package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.entities.User;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Map<Long, Integer>> friends = new HashMap<>();
    //added status
    //0 - requested friendship
    //1 - pending request
    //2 - friends

    @Override
    public Collection<User> getAll() {
        log.debug("UserDAO/getAll");
        return users.values();
    }

    @Override
    public User getById(long id) {
        validate(id);
        log.debug("UserDAO/getById: id {}", id);

        return users.get(id);
    }

    @Override
    public User create(User user) {
        validate(user);

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.debug("UserDAO/create - Added user: {}", user.toString());
        return user;
    }

    @Override
    public User update(User user) {
        validate(user.getId());

        User oldUser = users.get(user.getId());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setName(user.getName());
        log.debug("UserDAO/update - Updated user: {}", oldUser.toString());

        return oldUser;
    }

    @Override
    public User delete(long id) {
        validate(id);

        log.debug("UserDAO/delete - deleted user ID: {}", id);
        return users.remove(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        validate(userId, friendId);

        //If there is no any bonds - create blank map
        if (!friends.containsKey(userId)) {
            friends.put(userId, new HashMap<>());
        }

        //If user has bonds with friendId not equal to 1 - Exception
        if (friends.get(userId).containsKey(friendId) && friends.get(userId).get(friendId) != 1) {
            throw new ValidationException("Error: User already have friend with id " + friendId);
        }

        //If there is no bonds - Create pending request, else finalize frienship
        if (!friends.get(userId).containsKey(friendId))
            friends.get(userId).put(friendId, 0);
        else {
            friends.get(userId).put(friendId, 2);
        }

        //If friend does not have bonds - create blank
        if (!friends.containsKey(friendId)) {
            friends.put(friendId, new HashMap<>());
        }

        //If there is no bonds - Create pending request, else finalize frienship
        if (!friends.get(friendId).containsKey(userId)) {
            friends.get(friendId).put(userId, 1);
        } else {
            friends.get(friendId).put(userId, 2);
        }

        log.debug("UserDAO/addFriend - added friend ID {} for User ID {}", friendId, userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        validate(userId, friendId);

        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
        }

        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(userId);
        }

        log.debug("UserDAO/removeFriend - removed friend ID {} for User ID {}", friendId, userId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        validate(id);

        if (!friends.containsKey(id)) {
            return new HashSet<>();
        }

        log.debug("User/getFriends id: {}", id);
        return friends.get(id).entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
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
    public void validate(long... ids) {
        String notFound = Arrays.stream(ids)
                .filter(id -> !users.containsKey(id))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        if (!notFound.isBlank()) {
            throw new NotFoundException("User with ID " + notFound + " is not found");
        }
    }

    @Override
    public void validate(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("User:login - login cannot be null or blank");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("User:email - invalid Email format");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User:birthday - birthday cannot be in future");
        }
    }
}
