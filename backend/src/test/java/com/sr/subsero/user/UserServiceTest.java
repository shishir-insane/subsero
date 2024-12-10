package com.sr.subsero.user;

import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedException;
import com.sr.subsero.util.ReferencedWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        List<UserDTO> result = userService.findAll();

        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    public void testFindById() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.get(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindById_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.get(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testCreate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long result = userService.create(userDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdate() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.update(userId, userDTO);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdate_NotFound() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userId, userDTO));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testDelete() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDelete_ReferencedException() {
        Long userId = 1L;
        doThrow(new ReferencedException()).when(userRepository).deleteById(userId);

        assertThrows(ReferencedException.class, () -> userService.delete(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDelete_ReferencedWarning() {
        Long userId = 1L;
        ReferencedWarning referencedWarning = new ReferencedWarning();
        referencedWarning.setKey("user.subscription.user.referenced");
        referencedWarning.addParam(userId);
        doThrow(new ReferencedException(referencedWarning)).when(userRepository).deleteById(userId);

        assertThrows(ReferencedException.class, () -> userService.delete(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }
}