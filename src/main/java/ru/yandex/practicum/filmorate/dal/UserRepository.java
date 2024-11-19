package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.entities.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAll();

    User getById(long id);

    User create(User user);

    User update(User user);

    User delete(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Collection<User> getFriends(long id);

    void validate(User user);
}
