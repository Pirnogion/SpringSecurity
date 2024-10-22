package ru.gleb.SpringSecurity.model;

import lombok.*;
import ru.gleb.SpringSecurity.entity.Role;
import ru.gleb.SpringSecurity.entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Long id;
    private String name;
    private String email;
    private List<Role> roles;

    public static UserData setByUser(User user) {
        return new UserData(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
