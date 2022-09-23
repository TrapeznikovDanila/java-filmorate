package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interfaces.FriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendsDao;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendDao friendsDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsDao = friendsDao;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.info("Added a new user with id " + user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "update users set " +
                "user_email = ?, user_login = ?, user_name = ?, user_birthday = ?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Updated a user with id " + user.getId());
        return user;
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        List<User> userList = getAll();
        for (User user : userList) {
            users.put(user.getId(), user);
        }
        return users;
    }

    @Override
    public Optional<User> getUserById(long id) {
        String sqlQuery = "select * from users where user_id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setEmail(resultSet.getString("user_email"));
        user.setLogin(resultSet.getString("user_login"));
        user.setName(resultSet.getString("user_name"));
        user.setBirthday(resultSet.getTimestamp("user_birthday").toLocalDateTime().toLocalDate());
        user.setFriends(friendsDao.getFriends(user.getId()));
        return user;
    }
}
