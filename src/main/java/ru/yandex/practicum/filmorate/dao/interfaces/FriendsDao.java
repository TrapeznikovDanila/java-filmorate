package ru.yandex.practicum.filmorate.dao.interfaces;

import java.util.List;

public interface FriendsDao {

    List<Long> getFriends(long userId);

    void setFriends(long userId, long friendsId);

    boolean deleteFriend(long userId, long friendsId);
}
