package com.sr.subsero.user;

import com.sr.subsero.util.NotFoundException;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = new Role();
        role.setName(RoleEnum.ROLE_ADMIN);
        roleRepository.save(role);

        Role role2 = new Role();
        role2.setName(RoleEnum.ROLE_USER);
        roleRepository.save(role2);
    }

    @Test
    @Transactional
    public void testCreateUser() {
        User user = new User();
        user.setEmail("testuser@mail.com");
        user.setPasswordHash(passwordEncoder.encode("password"));

        User savedUser  = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("testuser@mail.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        user1.setEmail("testuser@mail.com");
        user1.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("testuser@mail.com");
        user2.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user2);
  
        List<UserDTO> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void testFindById() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("testuser@mail.com");
        user1.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user1);

        userRepository.findById(1L).orElse(null);

        UserDTO result = userService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindById_NotFound() {
        userRepository.deleteAll();

        assertThrows(NotFoundException.class, () -> userService.get(1L));
    }

    @Test
    public void testCreate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("testpassword");

        Long result = userService.create(userDTO);

        assertNotNull(result);       
    }

    @Test
    public void testUpdate() {
        Long userId = 1L;
        User user1 = new User();
        user1.setId(userId);
        user1.setEmail("testuser@mail.com");
        user1.setPasswordHash(passwordEncoder.encode("password"));
        user1 = userRepository.save(user1);
        
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user1.getEmail());
        userDTO.setPassword(user1.getPasswordHash());
        userDTO.setId(user1.getId());

        userService.create(userDTO);

        assertDoesNotThrow(() -> userService.update(userId, userDTO));
    }

    @Test
    public void testUpdate_NotFound() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("testpassword");

        userRepository.deleteAll();

        assertThrows(NotFoundException.class, () -> userService.update(userId, userDTO));
    }

    @Test
    @Transactional
    public void testDelete() {
        Long userId = 1L;
        
        User user1 = new User();
        user1.setEmail("testuser@mail.com");
        user1.setId(userId);
        user1.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("testuser@mail.com");
        user2.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user2);

        assertDoesNotThrow(() -> userService.delete(userId));
        assertNull(userRepository.findById(userId).orElse(null));
    }
}