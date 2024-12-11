package com.sr.subsero.user;

import com.sr.subsero.util.NotFoundException;
import com.sr.subsero.util.ReferencedException;
import com.sr.subsero.util.ReferencedWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserResourceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserResource userResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userResource.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        UserDTO user = new UserDTO();
        when(userService.get(userId)).thenReturn(user);

        ResponseEntity<UserDTO> response = userResource.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).get(userId);
    }

    @Test
    public void testGetUserById_NotFound() {
        Long userId = 1L;
        when(userService.get(userId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> userResource.getUser(userId));
        verify(userService, times(1)).get(userId);
    }

    @Test
    public void testCreateUser() {
        UserDTO user = new UserDTO();
        when(userService.create(user)).thenReturn(1L);

        ResponseEntity<Long> response = userResource.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody());
        verify(userService, times(1)).create(user);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UserDTO user = new UserDTO();
        doNothing().when(userService).update(userId, user);

        ResponseEntity<Long> response = userResource.updateUser(userId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody());
        verify(userService, times(1)).update(userId, user);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).delete(userId);

        ResponseEntity<Void> response = userResource.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testDeleteUser_ReferencedException() {
        Long userId = 1L;
        doThrow(new ReferencedException()).when(userService).delete(userId);

        assertThrows(ReferencedException.class, () -> userResource.deleteUser(userId));
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testDeleteUser_ReferencedWarning() {
        Long userId = 1L;
        ReferencedWarning referencedWarning = new ReferencedWarning();
        referencedWarning.setKey("user.subscription.user.referenced");
        referencedWarning.addParam(userId);
        doThrow(new ReferencedException(referencedWarning)).when(userService).delete(userId);

        assertThrows(ReferencedException.class, () -> userResource.deleteUser(userId));
        verify(userService, times(1)).delete(userId);
    }
}
