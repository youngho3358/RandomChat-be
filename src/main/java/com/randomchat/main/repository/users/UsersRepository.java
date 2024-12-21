package com.randomchat.main.repository.users;

import com.randomchat.main.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
