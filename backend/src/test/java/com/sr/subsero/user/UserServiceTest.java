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
        userService.delete(1L);
        
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("testpassword");

        var id = userService.create(userDTO);

        UserDTO result = userService.get(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
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
        
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testuser@mail.com");
        userDTO.setPassword("password");
        userDTO.setId(userId);

        var id = userService.create(userDTO);
        
        assertDoesNotThrow(() -> userService.update(id, userDTO));
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

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("testpassword");
        userService.create(userDTO);

        assertDoesNotThrow(() -> userService.delete(userId));
        assertNull(userRepository.findById(userId).orElse(null));
    }
}