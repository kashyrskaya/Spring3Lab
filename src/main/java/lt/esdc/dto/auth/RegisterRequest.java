package lt.esdc.dto.auth;

import lt.esdc.model.Role;

public record RegisterRequest(String username, String password, Role role) {
}