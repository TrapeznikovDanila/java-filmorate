package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class User {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @Size(max = 30)
    private String login;
    @Size(max = 200)
    private String name;
    @Past
    private LocalDate birthday;
    private List<Long> friends = new ArrayList<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("user_email", email);
        values.put("user_login", login);
        values.put("user_name", name);
        values.put("user_birthday", birthday);
        return values;
    }
}
