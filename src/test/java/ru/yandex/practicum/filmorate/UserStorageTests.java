package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FriendDaoImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTests {
    private final UserDbStorage userStorage;
    private final FriendDaoImpl friendsDao;


    @Test
    public void testFindUserById() {
        User user1 = new User();
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1999, 4, 1));
        user1.setEmail("name@mail.ru");
        user1.setLogin("login");
        userStorage.addUser(user1);

        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUser() {
        User user1 = new User();
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1999, 4, 1));
        user1.setEmail("name@mail.ru");
        user1.setLogin("login");
        long userId = userStorage.addUser(user1).getId();

        user1.setLogin("newLogin");
        user1.setId(userId);

        userStorage.updateUser(user1);

        Optional<User> userOptional = userStorage.getUserById(userId);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "newLogin")
                );
    }

    @Test
    public void testGetUsers() {
        User user1 = new User();
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1999, 4, 1));
        user1.setEmail("name@mail.ru");
        user1.setLogin("login");
        userStorage.addUser(user1);

        List<User> userList = userStorage.getAll();

        assertThat(userList).hasSize(1);
    }

    @Test
    public void testSetAndGetFriends() {
        User user1 = new User();
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1999, 4, 1));
        user1.setEmail("name@mail.ru");
        user1.setLogin("login");
        long user1Id = userStorage.addUser(user1).getId();

        User user2 = new User();
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(1999, 4, 1));
        user2.setEmail("name2@mail.ru");
        user2.setLogin("login2");
        long user2Id = userStorage.addUser(user2).getId();

        friendsDao.setFriends(user1Id, user2Id);

        List<Long> friendsList = friendsDao.getFriends(user1Id);

        assertThat(friendsList).hasSize(1);
    }

    @Test
    public void testSetAndDeleteFriends() {
        User user1 = new User();
        user1.setName("name");
        user1.setBirthday(LocalDate.of(1999, 4, 1));
        user1.setEmail("name@mail.ru");
        user1.setLogin("login");
        long user1Id = userStorage.addUser(user1).getId();

        User user2 = new User();
        user2.setName("name2");
        user2.setBirthday(LocalDate.of(1999, 4, 1));
        user2.setEmail("name2@mail.ru");
        user2.setLogin("login2");
        long user2Id = userStorage.addUser(user2).getId();

        friendsDao.setFriends(user1Id, user2Id);

        friendsDao.deleteFriend(user1Id, user2Id);

        List<Long> friendsList = friendsDao.getFriends(user1Id);

        assertThat(friendsList).hasSize(0);
    }
}
