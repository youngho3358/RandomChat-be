package com.randomchat.main.service.backOffice;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.backOffice.UserListDTO;
import com.randomchat.main.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchMemberService {

    private final UsersRepository usersRepository;

    public Long getTotalUserCount() {
        return usersRepository.countUsersWithRoleUser();
    }

    public Page<UserListDTO> getUserList(int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        return usersRepository.findUserWithLimitAndOffset(pageable);
    }

    public boolean deleteMember(String email) {
        Optional<Users> optionalUsers = usersRepository.findByEmail(email);
        if(optionalUsers.isPresent()) {
            usersRepository.delete(optionalUsers.get());
            return true;
        }else {
            return false;
        }
    }

    public List<UserListDTO> searchSpecificMember(String criteria, String inputData) {
        if(criteria.equals("nickname")) {
            return usersRepository.findSpecificUserByNickname(inputData);
        } else if(criteria.equals("email")) {
            return usersRepository.findSpecificUserByEmail(inputData);
        } else {
            throw new IllegalArgumentException("Invalid search criteria: " + criteria);
        }
    }
}
