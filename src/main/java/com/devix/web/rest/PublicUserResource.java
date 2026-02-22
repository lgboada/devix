package com.devix.web.rest;

import com.devix.domain.User;
import com.devix.repository.UserRepository;
import com.devix.service.dto.UserDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserRepository userRepository;

    public PublicUserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers() {
        LOG.debug("REST request to get all public Users");
        List<UserDTO> users = userRepository
            .findAllByActivatedIsTrue()
            .stream()
            .map(user -> new UserDTO(user.getId(), user.getLogin()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
