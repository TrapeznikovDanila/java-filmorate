package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(long id, long friendId) {
        isUserInMemory(id);
        isUserInMemory(friendId);
        if (getUserById(id).getFriends().contains(friendId)) {
            throw new ValidationException(String.format("User id %d already has a friend id %d", id, friendId));
        }
        getUserById(id).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(id);
        return getUserById(id);
    }

    public User deleteFriend(long id, long friendId) {
        isUserInMemory(id);
        isUserInMemory(friendId);
        if (!getUserById(id).getFriends().contains(friendId)) {
            throw new ValidationException(String.format("User id %d doesn't have a friend id %d", id, friendId));
        }
        getUserById(id).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(id);
        return getUserById(id);
    }

    public List<User> getFriendsList(long id) {
        List<User> friendsList = new ArrayList<>();
        for (Long i : getUserById(id).getFriends()) {
            for (User user : userStorage.getUsers().values()) {
                if (user.getId() == i) {
                    friendsList.add(user);
                }
            }
        }
        return friendsList;
    }

    public List<User> getSharedFriendsList(long id, long otherId) {
        List<User> sharedFriendsList = new ArrayList<>();
        for (Long firstFriendFriendsId : getUserById(id).getFriends()) {
            for (Long secondFriendFriendsId : getUserById(otherId).getFriends()) {
                if (firstFriendFriendsId == secondFriendFriendsId) {
                    sharedFriendsList.add(getUserById(firstFriendFriendsId));
                }
            }
        }
        return sharedFriendsList;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    private void isUserInMemory(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("User id %d not found", id));
        }
    }

    public User getUserById(long id) {
        isUserInMemory(id);
        return userStorage.getUsers().get(id);
    }
}
