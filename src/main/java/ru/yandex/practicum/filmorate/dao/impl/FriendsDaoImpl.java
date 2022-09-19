package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

import java.util.ArrayList;
import java.util.List;
@Component
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Long> getFriends(long userId) {
        List<Long> friendsList = new ArrayList<>();
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?",
                userId);
        while (friendsRows.next()) {
            friendsList.add(friendsRows.getLong("friend_id"));
        }
        return friendsList;
    }

    @Override
    public void setFriends(long userId, long friendsId) {
        String sqlQuery = "insert into friends(user_id, friend_id) values (?, ?)";

        jdbcTemplate.update(sqlQuery,
                userId,
                friendsId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendsId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendsId) > 0;
    }
}
