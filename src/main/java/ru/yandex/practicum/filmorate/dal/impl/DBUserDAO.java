package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.UserDAO;
import ru.yandex.practicum.filmorate.entities.User;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@Primary
public class DBUserDAO extends BaseRepository<User> implements UserDAO {


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


    public DBUserDAO(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getById(long id) {
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

        return user;
    }

    @Override
    public User delete(long id) {
        User user = getById(id);

        update(DELETE_QUERY, id);

        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public Collection<User> getFriends(long id) {
        return List.of();
    }

    @Override
    public Collection<User> getPending(long id) {
        return List.of();
    }

    @Override
    public Collection<User> getRequests(long id) {
        return List.of();
    }

    @Override
    public void validate(User user) {
        if (user.getId()>0){
            findOne(FIND_BY_ID_QUERY,user.getId()).orElseThrow(
                    () -> new NotFoundException("User is not found with id " + user.getId())
            );
        }else if(user.getId()<0){
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
}
