package com.randomchat.main.repository.users;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.backOffice.UserListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = 'USER'")
    Long countUsersWithRoleUser();

    @Query("SELECT new com.randomchat.main.dto.backOffice.UserListDTO(u.email, u.nickname, u.gender) FROM Users u WHERE u.role=USER ORDER BY u.id ASC")
    Page<UserListDTO> findUserWithLimitAndOffset(Pageable pageable);

    @Query("SELECT new com.randomchat.main.dto.backOffice.UserListDTO(u.email, u.nickname, u.gender) FROM Users u WHERE u.email LIKE %:inputData%")
    List<UserListDTO> findSpecificUserByEmail(@Param("inputData") String inputData);

    @Query("SELECT new com.randomchat.main.dto.backOffice.UserListDTO(u.email, u.nickname, u.gender) FROM Users u WHERE u.nickname LIKE %:inputData%")
    List<UserListDTO> findSpecificUserByNickname(@Param("inputData") String inputData);
}
