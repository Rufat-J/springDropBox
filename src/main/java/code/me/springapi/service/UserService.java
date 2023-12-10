package code.me.springapi.service;

import code.me.springapi.dto.LoginSuccess;
import code.me.springapi.dto.RegisterSuccess;
import code.me.springapi.exception.InvalidLoginException;
import code.me.springapi.exception.RegistrationFailureException;
import code.me.springapi.model.User;
import code.me.springapi.repository.UserRepository;
import code.me.springapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     * @return RegisterSuccess object with a success message.
     * @throws RegistrationFailureException If the registration fails.
     */
    public RegisterSuccess register(String username, String password) throws RegistrationFailureException {
        if (userRepository.existsByUsername(username)) {
            throw new RegistrationFailureException("Registration failed: Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        try {
            userRepository.save(newUser);
            return new RegisterSuccess("Account with username: {" + username + "} Registered successfully");
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationFailureException("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RegistrationFailureException("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Performs user login.
     *
     * @param username Username for login.
     * @param password Password for login.
     * @return LoginSuccess object with a success message and generated token.
     * @throws InvalidLoginException If the login fails.
     */
    public LoginSuccess login(String username, String password) throws InvalidLoginException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (password.equals(user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user);
                return new LoginSuccess("Login successful", token);
            }
        }

        throw new InvalidLoginException("Login failed, incorrect username or password");
    }

    /**
     * Retrieves a user by username.
     *
     * @param username Username of the user to be retrieved.
     * @return The retrieved user or null if not found.
     */
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId ID of the user to be retrieved.
     * @return The retrieved user or null if not found.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
