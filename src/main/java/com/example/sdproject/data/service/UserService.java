package com.example.sdproject.data.service;

import com.example.sdproject.data.dto.UserDTO;
import com.example.sdproject.data.entity.User;
import com.example.sdproject.data.exception.PasswordMismatchException;
import com.example.sdproject.data.exception.UserAlreadyExistException;
import com.example.sdproject.data.exception.UserNotFoundException;
import com.example.sdproject.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public Optional<User> getUser(Integer id) { return userRepository.findById(id); }

    public void addUser(User user) throws UserAlreadyExistException {
        if (usernameExists(user.getUsername())) {
            throw new UserAlreadyExistException("Username taken");
        }
        userRepository.save(user);
    }

    public void addUser(UserDTO user) throws UserAlreadyExistException, PasswordMismatchException {
        if (usernameExists(user.getUsername())) {
            throw new UserAlreadyExistException("Username taken");
        }

        if (!user.getPassword().equals(user.getMatchingPassword())) {
            throw new PasswordMismatchException("Passwords don't match");
        }

        User u = new User();
        u.setUsername(user.getUsername());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        u.setEmail(user.getEmail());
        u.setTelephone(user.getTelephone());
        u.setRoles("ROLE_USER" + (user.isAdmin() ? ",ROLE_ADMIN" : ""));

        userRepository.save(u);
    }

    public void deleteUser(Integer id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    public void deleteAll() { userRepository.deleteAll(); }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
