package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAll();

    User getById(long id);

    User create(User user);

    User update(User user);

    User delete(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long userId, long friendId);

}
