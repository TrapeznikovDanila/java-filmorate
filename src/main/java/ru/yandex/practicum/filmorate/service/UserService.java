package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ObjectConflictException;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    //private long id = 0;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        checkUser(user);
        if (userStorage.getUsers().containsValue(user)) {
            throw new ObjectConflictException("This user was already added");
        }
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        checkUser(user);
        isUserInMemory(user.getId());
        return userStorage.updateUser(user);
    }

    public User addFriends(long id, long friendId) {
        isUserInMemory(id);
        isUserInMemory(friendId);
        if (friendsDao.getFriends(friendId).contains(id)) {
            throw new ValidationException(String.format("User id %d already has a friend id %d",
                    id, friendId));
        }
        friendsDao.setFriends(id, friendId);
        return getUserById(id).get();
    }

    public void deleteFriend(long id, long friendId) {
        isUserInMemory(id);
        isUserInMemory(friendId);
        if (!friendsDao.getFriends(id).contains(friendId)) {
            throw new ValidationException(String.format("User id %d doesn't have a friend id %d",
                    id, friendId));
        }
        friendsDao.deleteFriend(id, friendId);
    }

    public List<User> getFriendsList(long id) {
        isUserInMemory(id);
        List<User> userList = new ArrayList<>();
        List<Long> longList = friendsDao.getFriends(id);
        for (Long l : longList) {
            userList.add(userStorage.getUserById(l).get());
        }
        return userList;
    }

    public List<User> getSharedFriendsList(long id, long otherId) {
        List<User> sharedFriendsList = new ArrayList<>();
        for (Long firstFriendFriendsId : getUserById(id).get().getFriends()) {
            for (Long secondFriendFriendsId : getUserById(otherId).get().getFriends()) {
                if (firstFriendFriendsId == secondFriendFriendsId) {
                    sharedFriendsList.add(getUserById(firstFriendFriendsId).get());
                }
            }
        }
        return sharedFriendsList;
    }

    public void isUserInMemory(long id) {
        if (id < 0 || userStorage.getUserById(id) == null) {
            log.info("The user with the id " + id + "is missing from the database");
            throw new ObjectNotFoundException(String.format("User id %d not found", id));
        }
    }

    public Optional<User> getUserById(long id) {
        isUserInMemory(id);
        return userStorage.getUserById(id);
    }

    public void checkUser(User user) {
        if (user != null) {
            if ((user.getEmail() == null) || (!user.getEmail().contains("@"))) {
                throw new ValidationException("The email cannot be empty and must contain the character @");
            } else if ((user.getLogin() == null) || (user.getLogin().contains(" "))) {
                throw new ValidationException("The login cannot be empty and contain spaces");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("The date of birth cannot be in the future");
            }
        }
    }
}
