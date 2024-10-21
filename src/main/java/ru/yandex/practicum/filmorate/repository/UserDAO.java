package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserDAO {
    Collection<User> getAll();

    User getById(long id);

    User create(User user);

    User update(User user);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Collection<User> getFriends(long id);

    void checkUsers(long... ids);
}
