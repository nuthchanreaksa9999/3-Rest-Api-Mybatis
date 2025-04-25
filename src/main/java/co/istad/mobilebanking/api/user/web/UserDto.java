package co.istad.mobilebanking.api.user.web;

public record UserDto(

        String name,
        String gender,
        String  studentCardId,
        Boolean isStudent

) {
}
