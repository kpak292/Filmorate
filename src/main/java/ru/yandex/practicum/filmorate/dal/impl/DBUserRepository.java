package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.entities.User;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Repository
@Primary
public class DBUserRepository extends BaseRepository<User> implements UserRepository {
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM   users
            WHERE  status_id = 0;""";

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM   users
            WHERE  status_id = 0
                   AND id = ?;""";

    private static final String INSERT_QUERY = """
            INSERT INTO users
                        (email,
                         name,
                         login,
                         birthday)
            VALUES      (?,
                         ?,
                         ?,
                         ?)""";

    private static final String UPDATE_QUERY = """
            UPDATE users
            SET    email = ?,
                   name = ?,
                   login = ?,
                   birthday = ?
            WHERE  id = ?""";

    private static final String DELETE_QUERY = """
            UPDATE users
            SET    status_id = 1
            WHERE  id = ?""";

    private static final String FIND_FRIENDS_BY_USER_ID_QUERY = """
            SELECT u.*
            FROM   users u
                   INNER JOIN friends f
                           ON u.id = f.friend_id
            WHERE  u.status_id = 0
                   AND f.status_id = 1
                   AND f.user_id = ?;""";

    private static final String ADD_FRIEND_QUERY = """
            INSERT INTO friends
                        (user_id,
                         friend_id,
                         status_id)
            VALUES      (?,
                         ?,
                         1)""";

    private static final String DELETE_FRIEND_QUERY = """
            UPDATE friends
            SET    status_id = 2
            WHERE  user_id = ?
                   AND friend_id = ?""";

    private static final String DELETE_ALL_FRIENDS_QUERY = """
            UPDATE friends
            SET    status_id = 2
            WHERE  user_id = ?""";

    private static final String DELETE_FROM_ALL_FRIENDS_QUERY = """
            UPDATE friends
            SET    status_id = 2
            WHERE  friend_id = ?""";


    public DBUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public Collection<User> getAll() {
        log.debug("UserDAO/getAll");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getById(long id) {
        log.debug("UserDAO/getById: id {}", id);
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(
                        () -> new NotFoundException("User is not found with id " + id)
                );
    }

    @Override
    public User create(User user) {
        validate(user);

        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday());

        user.setId(id);

        log.debug("UserDAO/create - Added user: {}", user.toString());
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);

        update(UPDATE_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());

        log.debug("UserDAO/update - Updated user: {}", user.toString());
        return user;
    }

    @Override
    public User delete(long id) {
        User user = getById(id);

        update(DELETE_QUERY, id);

        removeAllFriends(id);

        log.debug("UserDAO/delete - deleted user ID: {}", id);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        getById(userId);
        getById(friendId);

        long id = insert(ADD_FRIEND_QUERY,
                userId,
                friendId);

        log.debug("UserDAO/addFriend - added friend ID {} for User ID {}", friendId, userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        getById(userId);
        getById(friendId);

        update(DELETE_FRIEND_QUERY, userId, friendId);
        log.debug("UserDAO/removeFriend - removed friend ID {} for User ID {}", friendId, userId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        getById(id);

        log.debug("User/getFriends id: {}", id);
        return findMany(FIND_FRIENDS_BY_USER_ID_QUERY, id);
    }

    @Override
    public void validate(User user) {
        if (user.getId() > 0) {
            findOne(FIND_BY_ID_QUERY, user.getId()).orElseThrow(
                    () -> new NotFoundException("User is not found with id " + user.getId())
            );
        } else if (user.getId() < 0) {
            throw new ValidationException("User:id - id cannot be negative");
        }

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

    public void removeAllFriends(long id) {
        update(DELETE_ALL_FRIENDS_QUERY, id);
        update(DELETE_FROM_ALL_FRIENDS_QUERY, id);
    }
}
