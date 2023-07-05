package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.Userprofile;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserProfileRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final RoleRepository roleRepository;

    private final DealerRepository dealerRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO registerAccount(RegisterDto registerDto) {
        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(registerDto);

        User user = insertUser(registerDto);

        try {
            userRepository.save(user);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Create account successfully");
        } catch (UserAlreadyExistException e) {
            response.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
            response.setMessage("Service unavailable");
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
            dealer.setAdharShopact(registerDto.getAdharShopact());
            dealer.setArea(registerDto.getArea());
            dealer.setCity(registerDto.getCity());
            dealer.setFirstname(registerDto.getFirstName());
            dealer.setLastName(registerDto.getLastName());
            dealer.setMobileNo(registerDto.getMobileNo());
            dealer.setShopName(registerDto.getShopName());
            dealer.setEmail(registerDto.getEmail());

            user.setDealer(dealer);
            dealer.setUser(user); // Set the user instance in the dealer
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
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username already exists");
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
                    response.setMessage("Password changed successfully");
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
            throw new UserNotFoundExceptions("No user found with this id");
        }

        // Return the response object
        return response;
    }

    @Override
    public List<UserProfileDto> getAllUsers(int pageNo) {
        // Retrieve all user profiles from the repository
        User user= new User();
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

    @Override
    public Userprofile getUser(int id) {

        Optional<Userprofile> userProfile= userProfileRepository.findById(id);

        if (userProfile.isPresent()){
            System.out.println("printing "+userProfile);
            return userProfile.get();

        }else {

         throw new UserNotFoundExceptions("No user found with this id");
        }
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
            response.setMessage("User details edited successfully");
        } else {
            // if the user is not found, set the response code and throw a UserNotFoundException
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            throw new UserNotFoundExceptions("No user found with this id");
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
            response.setMessage("User deleted successfully");

        } else {
            // Set the error code and message in the response DTO
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            // Throw a UserNotFoundException indicating that no user was found with the given ID
            throw new UserNotFoundExceptions("No user found with this id");
        }

        // Return the response DTO
        return response;
    }

}
