package co.istad.mobilebanking.api.auth.web;

public record AuthDto(

        String tokenType,

        String accessToken

        // String authHeader

) {
}
