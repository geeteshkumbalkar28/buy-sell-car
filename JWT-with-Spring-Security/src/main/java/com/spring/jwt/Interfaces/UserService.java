package com.spring.jwt.Interfaces;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.utils.BaseResponseDTO;

import java.util.List;

public interface UserService {
    BaseResponseDTO registerAccount(RegisterDto registerDto);

    /**
     This method is used to change the password for a user.
     @param id The ID of the user for whom the password needs to be changed.
     @param passwordChange An object containing the old password, new password, and confirm password.
     @return BaseResponseDTO An object representing the response of the password change operation.
     */
    BaseResponseDTO changePassword(int id, PasswordChange passwordChange);

    /**
     This method edits the details of a user.
     @param userProfileDto - the DTO object containing the updated user details
     @param id - the id of the user to be edited
     @return BaseResponseDTO - the response object indicating the success or failure of the operation
     */
    BaseResponseDTO editUser(UserProfileDto userProfileDto, int id);

    /**
     This method is used to remove a user from the database with the given id.
     It is annotated with @Transactional to ensure that the database transaction is managed properly.
     @param id - The id of the user to be removed.
     @return - The response object containing the status code and message of the operation.
     */
    BaseResponseDTO removeUser(int id);

    /**
     * Retrieves a list of user profiles based on the specified page number.
     * @param pageNo The page number to retrieve.
     * @return A list of user profiles as UserProfileDto objects.
     * @throws PageNotFoundException If the specified page is not found.
     * @throws UserNotFoundException If no user profiles are found.
     */
    List<UserProfileDto> getAllUsers(int pageNo);

    /**
     * Retrieves a user profile by ID and converts it to a DTO (Data Transfer Object).
     *
     * @param id The ID of the user profile to retrieve.
     * @return The user profile DTO.
     * @throws UserNotFoundExceptions If the user profile is not found by the provided ID.
     */
    UserProfileDto getUser(int id);

    /**
     Updates the reset password token for a user with the specified email.
     @param token The reset password token to be set for the user.
     @param email The email of the user whose reset password token is being updated.
     @throws UserNotFoundExceptions If no user is found with the given email.
     */
    void updateResetPassword(String token, String email);

    /**
     Sends a password reset email to the specified email address.
     If the email belongs to a registered user, an email is sent with a password reset link.
     If the email does not belong to any registered user, a UserNotFoundException is thrown.
     @param email The email address of the user requesting a password reset.
     @param resetPasswordLink The password reset link to be included in the email.
     @param domain The domain of the application.
     @return A ResponseDto indicating the status of the operation.
     @throws UserNotFoundExceptions If the email does not belong to any registered user.
     */
    ResponseDto forgotPass(String email, String resetPasswordLink, String domain) throws UserNotFoundExceptions;

    /**
     * Updates the password for a user based on a reset password token.
     *
     * @param token         The reset password token used to identify the user.
     * @param newPassword  The new password to be set for the user.
     * @return              A ResponseDto object indicating the status and message of the operation.
     */
    ResponseDto updatePassword(String token, String newPassword);
}
