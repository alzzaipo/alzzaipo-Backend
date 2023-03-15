package com.alzzaipo.service;

import com.alzzaipo.domain.dto.UserJoinRequestDto;
import com.alzzaipo.domain.user.User;
import com.alzzaipo.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findMemberById(Long memberId) { return userRepository.findById(memberId); }

    public Optional<User> findMemberByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
