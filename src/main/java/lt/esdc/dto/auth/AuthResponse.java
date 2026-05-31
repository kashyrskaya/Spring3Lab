package lt.esdc.dto.auth;

public record AuthResponse(String accessToken, String refreshToken) {
}