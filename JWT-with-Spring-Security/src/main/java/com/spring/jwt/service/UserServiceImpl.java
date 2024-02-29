package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserProfileRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.Interfaces.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO registerAccount(RegisterDto registerDto) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(registerDto);

        User user = insertUser(registerDto);


        try {
            userRepository.save(user);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Account Created");
        } catch (UserAlreadyExistException e) {
            response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.setMessage("User already exist");
        }catch (BaseException e){
            response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.setMessage("Invalid role");
        }
        return response;
    }

    private User insertUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setMobileNo(registerDto.getMobileNo());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(registerDto.getRoles());
        roles.add(role);
        user.setRoles(roles);

        if (role.getName().equals("USER")) {
            Userprofile profile = new Userprofile();
            profile.setAddress(registerDto.getAddress());
            profile.setCity(registerDto.getCity());
            profile.setFirstName(registerDto.getFirstName());
            profile.setLastName(registerDto.getLastName());

            user.setProfile(profile);
            profile.setUser(user);
        } else if (role.getName().equals("DEALER")) {
            Dealer dealer = new Dealer();
            dealer.setAddress(registerDto.getAddress());
            dealer.setArea(registerDto.getArea());
            dealer.setCity(registerDto.getCity());
            dealer.setStatus(registerDto.status);
            dealer.setFirstname(registerDto.getFirstName());
            dealer.setLastName(registerDto.getLastName());
            dealer.setMobileNo(registerDto.getMobileNo());
            dealer.setShopName(registerDto.getShopName());
            dealer.setEmail(registerDto.getEmail());

            user.setDealer(dealer);
            dealer.setUser(user);
        }

        return user;
    }

    private void validateAccount(RegisterDto registerDto) {
        // validate null data
        if (ObjectUtils.isEmpty(registerDto)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not be empty");
        }

        // validate duplicate username
        User user = userRepository.findByEmail(registerDto.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            throw new UserAlreadyExistException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username already exists");
        }

        // validate role
        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        if (!roles.contains(registerDto.getRoles())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }
    }

    @Override
    public BaseResponseDTO changePassword(int id, PasswordChange passwordChange) {
        // Create a new BaseResponseDTO object to store the response
        BaseResponseDTO response = new BaseResponseDTO();

        // Retrieve the user with the given id from the userRepository
        Optional<Userprofile> userOptional = userProfileRepository.findById(id);

        // Check if the user exists
        if (userOptional.isPresent()) {
            User user= userOptional.get().getUser();

            // Check if the old password matches the stored password for the user
            if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {

                // Check if the new password and confirm password match
                if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
                    // Encode and set the new password for the user
                    user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    userRepository.save(user);

                    // Set the response code and message for a successful password change
                    response.setCode(String.valueOf(HttpStatus.OK.value()));
                    response.setMessage("Password changed");
                } else {
                    // Set the response code and throw an exception for password mismatch
                    response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                    throw new InvalidPasswordException("New password and confirm password does not match");
                }
            } else {
                // Set the response code and throw an exception for invalid password
                response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                throw new InvalidPasswordException("Invalid Password");
            }

        } else {
            // Throw an exception if no user is found with the given id
            throw new UserNotFoundExceptions("No user found");
        }

        // Return the response object
        return response;
    }

    @Override
    public List<UserProfileDto> getAllUsers(int pageNo) {
        // Retrieve all user profiles from the repository
        //User user= new User();
        List<Userprofile> listOfUsers = userProfileRepository.findAll();


        // Check if the specified page number exceeds the available number of user profiles
        if ((pageNo * 10) > listOfUsers.size() - 1) {
            throw new PageNotFoundException("Page not found");
        }

        // Check if there are no user profiles available
        if (listOfUsers.size() <= 0) {
            throw new UserNotFoundExceptions("User not found", HttpStatus.NOT_FOUND);
        }

       // System.out.println("List of users: " + listOfUsers.size());

        // Create a new list to store the converted UserProfileDto objects
        List<UserProfileDto> listOfUserDto = new ArrayList<>();

        // Determine the starting and ending indices for the specified page
        int pageStart = pageNo * 25;
        int pageEnd = pageStart + 25;

        // Calculate the remaining number of user profiles after the starting index
        int diff = (listOfUsers.size()) - pageStart;

        // Iterate over the user profiles and convert them to UserProfileDto objects
        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            // If the starting index exceeds the available user profiles, exit the loop
            if (pageStart > listOfUsers.size()) {
                break;
            }
            Optional<User> users=userRepository.findById(listOfUsers.get(counter).getUser().getId());
            if(users.isEmpty()){throw new UserNotFoundExceptions("User not found ");}
          // System.out.println("*");

            // Convert the user profile to a UserProfileDto object and add it to the list
            UserProfileDto userProfileDto = new UserProfileDto(listOfUsers.get(counter),users.get());

            listOfUserDto.add(userProfileDto);

            // If the remaining number of profiles is equal to the current iteration, exit the loop
            if (diff == i) {
                break;
            }
        }
        // return the list of the users
        return listOfUserDto;
    }

    private UserProfileDto convertToDto(Userprofile userprofile,User user ) {
        //User user = new User();
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(userprofile.getId());
        userProfileDto.setAddress(userprofile.getAddress());
        userProfileDto.setCity(userprofile.getCity());
        userProfileDto.setFirstName(userprofile.getFirstName());
        userProfileDto.setLastName(userprofile.getLastName());
        userProfileDto.setMobile_no(user.getMobileNo());
        userProfileDto.setEmail(user.getEmail());
        return userProfileDto;
    }

    @Override
    public UserProfileDto getUser(int id) {
        // Retrieve the user profile by ID from the repository
        Optional<Userprofile> userOptional = userProfileRepository.findById(id);

        // Check if the user profile is present
        if (userOptional.isEmpty()) {
            // Throw an exception if the user profile is not found
            throw new UserNotFoundExceptions("User not found");
        }

        // Assuming the Userprofile entity has a "user" field representing the associated User
        // Retrieve the User object associated with the Userprofile
        User user = userOptional.get().getUser();

        // Convert the Userprofile and User objects to a UserProfileDto object
        return convertToDto(userOptional.get(), user);
    }

    @Override
    public BaseResponseDTO editUser(UserProfileDto userProfileDto, int id) {

        // create a new response object
        BaseResponseDTO response = new BaseResponseDTO();

        // find the user with the given id in the repository
        Optional<Userprofile> user = userProfileRepository.findById(id);
        if(user.isPresent()){
            // if the user is found, update the user's details with the values from the DTO object
            user.get().setFirstName(userProfileDto.getFirstName());
            user.get().setLastName(userProfileDto.getLastName());
            user.get().setAddress(userProfileDto.getAddress());
            user.get().getUser().setMobileNo(userProfileDto.getMobile_no());
            user.get().getUser().setEmail(userProfileDto.getEmail());
            user.get().setCity(userProfileDto.getCity());

            // save the updated user in the repository
            userProfileRepository.save(user.get());

            // set the response code and message indicating the success of the operation
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User edited");
        } else {
            // if the user is not found, set the response code and throw a UserNotFoundException
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            throw new UserNotFoundExceptions("No user found");
        }

        // return the response object
        return response;
    }

    @Override
    @Transactional
    public BaseResponseDTO removeUser(int id) {
        // Create a new base response DTO
        BaseResponseDTO response = new BaseResponseDTO();

        // Find the user with the given ID in the user profile repository
        Optional<Userprofile> user = userProfileRepository.findById(id);
        // Check if the user exists in the repository
        if(user.isPresent()){

           User users= user.get().getUser();

            // Delete the user from the repository
            userRepository.DeleteById(users.getId());

            // Set the success code and message in the response DTO
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User deleted");

        } else {
            // Set the error code and message in the response DTO
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            // Throw a UserNotFoundException indicating that no user was found with the given ID
            throw new UserNotFoundExceptions("No user found");
        }

        // Return the response DTO
        return response;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public ResponseDto forgotPass(String email, String resetPasswordLink, String domain) throws UserNotFoundExceptions {
        ResponseDto response = new ResponseDto();

        // Find the user with the specified email
        User user = userRepository.findByEmail(email);

        if (user != null) {
            // Set the email message content
            String message = "Hello this is Aniket";

            // Set the password reset link
            String resetLink = resetPasswordLink;

            // Set the email subject
            String subject = "Checking: confirmation";

            // Set the sender's email address
            String from = "b.aniket1414@gmail.com";

            // Set the recipient's email address
            String to = email;

            // Send the email using the sendEmail() method
            sendEmail(message, subject, to, from, resetLink, domain);

            // Update the response with success status and message
            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Email sent");

        } else {
            // The user with the specified email was not found
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
            response.setMessage("User not found");
            throw new UserNotFoundExceptions("User not found");
        }

        // Return the response
        return response;
    }

    // @Override
    public void sendEmail(String message, String subject, String to, String from, String resetLink, String domain) {


        // SMTP server for Gmail
        String host = "smtp.gmail.com";

        // Getting the system properties
        Properties properties = System.getProperties();

        System.out.println(properties);

        // Setting important information to the properties object
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Creating a session with the properties and an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Return the email address and password for authentication
                return new PasswordAuthentication("b.aniket1414@gmail.com", "egmqlowlfodymfzw");
            }

        });

        // Composing the email content
        String content = "To reset your password, click here: " + resetLink.replace("169.254.63.118:5173", domain);

        // Creating a MimeMessage object for the session
        MimeMessage m = new MimeMessage(session);

        try {
            // Setting the sender of the email
            m.setFrom(from);

            // Adding the recipient to the message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Adding the subject to the message
            m.setSubject(subject);

            // Adding the content to the message
            m.setText(content);

            // Sending the message
            Transport.send(m);

        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

    public void updateResetPassword(String token, String email) throws UserNotFoundExceptions {
        // Find the user with the specified email in the user repository
        User user = userRepository.findByEmail(email);

        // Check if a user was found with the given email
        if (user != null) {
            // Set the reset password token for the user
            user.setResetPasswordToken(token);
            // Save the updated user in the user repository
            userRepository.save(user);
        } else {
            // Throw a UserNotFoundExceptions indicating that no user was found with the given email
            throw new UserNotFoundExceptions("could not find any user with this email");
        }
    }

    public ResponseDto updatePassword(String token, String newPassword) {
        // Create a new ResponseDto object to hold the response details
        ResponseDto response = new ResponseDto();

        // Find the user based on the reset password token
        User user = userRepository.findByResetPasswordToken(token);

        if (user != null) {
            // Create an instance of BCryptPasswordEncoder to encode the new password
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            // Encode the new password using BCryptPasswordEncoder
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

            // Update the user's password with the encoded password
            user.setPassword(encodedPassword);

            // Clear the reset password token as it is no longer needed
            user.setResetPasswordToken(null);

            // Save the updated user object in the repository
            userRepository.save(user);

            // Set the status and message in the response indicating a successful password update
            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Successful");
        } else {
            // If the user is not found based on the reset password token, handle the error
            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Email sent");

            // Throw a UserNotFoundExceptions indicating that something went wrong
            throw new UserNotFoundExceptions("Something went wrong");
        }

        // Return the response object
        return response;
    }

}
