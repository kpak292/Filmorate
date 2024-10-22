package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDAO;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserDAO repository;

    @Override
    public Collection<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getById(long id) {
        return repository.getById(id);
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return repository.create(user);
    }

    @Override
    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return repository.update(user);
    }

    @Override
    public User delete(long id) {
        return repository.delete(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        repository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        repository.removeFriend(userId, friendId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        return repository.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long friendId) {
        Collection<User> userFriends = repository.getFriends(userId);
        Collection<User> friendFriends = repository.getFriends(friendId);

        return userFriends.stream()
                .filter(friendFriends::contains)
                .toList();
    }

}
