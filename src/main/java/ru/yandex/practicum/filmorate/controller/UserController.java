package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> getAll() {
        return userService.getAll();
    }

    @Override
    @PostMapping
    public User addObject(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @Override
    @PutMapping
    public User updateObject(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable Long id) {
        return userService.getFriendsList(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getFriendsList(@PathVariable long id, @PathVariable long otherId) {
        return userService.getSharedFriendsList(id, otherId);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }
}
