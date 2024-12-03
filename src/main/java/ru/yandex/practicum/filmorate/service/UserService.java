package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.entities.User;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public User getById(long id) {
        return repository.getById(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return repository.create(user);
    }

    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return repository.update(user);
    }

    public User delete(long id) {
        return repository.delete(id);
    }

    public void addFriend(long userId, long friendId) {
        repository.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        repository.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(long id) {
        return repository.getFriends(id);
    }

    public Collection<User> getCommonFriends(long userId, long friendId) {
        Collection<User> userFriends = repository.getFriends(userId);
        Collection<User> friendFriends = repository.getFriends(friendId);

        return userFriends.stream()
                .filter(friendFriends::contains)
                .toList();
    }

}
