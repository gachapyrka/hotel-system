package com.example.hotel.controller;

import com.example.hotel.domain.*;
import com.example.hotel.repo.AccountRepo;
import com.example.hotel.repo.EmployeeProfileRepo;
import com.example.hotel.repo.HotelRepo;
import com.example.hotel.repo.UserProfileRepo;
import com.example.hotel.service.KeyGenerationService;
import com.example.hotel.service.MailService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class NoUserController {
    private final AccountRepo accountRepo;
    private final KeyGenerationService keyGenerationService;
    private final MailService mailService;
    private final UserProfileRepo userProfileRepo;
    private final EmployeeProfileRepo employeeProfileRepo;
    private final HotelRepo hotelRepo;

    public NoUserController(AccountRepo accountRepo, KeyGenerationService keyGenerationService, MailService mailService,
                            UserProfileRepo userProfileRepo, EmployeeProfileRepo employeeProfileRepo, HotelRepo hotelRepo) {
        this.accountRepo = accountRepo;
        this.keyGenerationService = keyGenerationService;
        this.mailService = mailService;
        this.userProfileRepo = userProfileRepo;
        this.employeeProfileRepo = employeeProfileRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        if(accountRepo.findByUsername("admin@admin.com") == null){
            Account account = new Account();
            account.setUsername("admin@admin.com");
            account.setPassword("admin");
            account.setRole(Role.ADMIN);
            account.setActive(true);

            accountRepo.save(account);
        }

        return "index";
    }

    @GetMapping("/registration-user")
    public String registrationUserGet(Map<String, Object> model) {
        return "registrationUser";
    }

    @PostMapping("/registration-user")
    public String registrationUserPost(@RequestParam String credentials,
                                       @RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String repeatPassword,
                                       @RequestParam String passport,
                                       @RequestParam String telephone,
                                       Map<String, Object> model) {
        model.put("username", username);
        model.put("password", password);
        model.put("repeatPassword", repeatPassword);
        model.put("passport", passport);
        model.put("credentials", credentials);
        model.put("telephone", telephone);

        if (accountRepo.findByUsername(username) != null) {
            model.put("errorUsername", "Данный логин уже занят!");
            return "registrationUser";
        }

        String key = keyGenerationService.generateKey(username, Role.USER, false);
        String title = "Код подтверждения.";
        String text = "Ваш код подтверждения: " + key + "\n";
        text += "Никому не сообщайте и не передавайте данный код во избежание кражи ваших личных данных!";

        mailService.send(title, text, username);

        return "confirmation";
    }

    @PostMapping("/confirmation")
    public String confirmationPost(@RequestParam String credentials,
                                       @RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String repeatPassword,
                                       @RequestParam String passport,
                                       @RequestParam String telephone,
                                       @RequestParam String key,
                                       Map<String, Object> model) {

        if(keyGenerationService.matchKey(key, username, Role.USER, false) == null){
            model.put("username", username);
            model.put("password", password);
            model.put("repeatPassword", repeatPassword);
            model.put("passport", passport);
            model.put("credentials", credentials);
            model.put("telephone", telephone);
            model.put("error", "Неверный код подтверждения");
            model.put("key", key);

            return "confirmation";
        }

        Account account = new Account();
        account.setRole(Role.USER);
        account.setUsername(username);
        account.setPassword(password);
        account.setActive(true);

        accountRepo.save(account);

        UserProfile userProfile = new UserProfile();
        userProfile.setAccount(accountRepo.findByUsername(username));
        userProfile.setPassport(passport);
        userProfile.setCredentials(credentials);
        userProfile.setTelephone(telephone);

        userProfileRepo.save(userProfile);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        return "logout";
    }

    @GetMapping("/registration-employee")
    public String registrationEmployeeGet(Map<String, Object> model) {
        return "registrationEmployee";
    }

    @PostMapping("/registration-employee")
    public String registrationEmployeePost(@RequestParam String credentials,
                                           @RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String repeatPassword,
                                           @RequestParam String telephone,
                                           @RequestParam String key,
                                           Map<String, Object> model) {
        String uname = keyGenerationService.matchKey(key, username, Role.EMPLOYEE, false);
        if(uname != null){

            Account account = new Account();
            account.setRole(Role.EMPLOYEE);
            account.setUsername(username);
            account.setPassword(password);
            account.setActive(true);

            accountRepo.save(account);

            EmployeeProfile employeeProfile = new EmployeeProfile();
            employeeProfile.setAccount(accountRepo.findByUsername(username));
            employeeProfile.setCredentials(credentials);
            employeeProfile.setTelephone(telephone);

            Hotel hotel = new Hotel();
            String hotelName = "Без названия " + username;
            hotel.setName(hotelName);
            hotel.setDescription("");

            hotelRepo.save(hotel);

            employeeProfile.setHotel(hotelRepo.findByName(hotelName).get(0));
            employeeProfileRepo.save(employeeProfile);


            model.put("username", username);
            model.put("password", password);
            model.put("repeatPassword", repeatPassword);
            model.put("credentials", credentials);
            model.put("telephone", telephone);
            model.put("key", key);

            return "redirect:/login";
        }

        //Add new employee to already created hotel
        uname = keyGenerationService.matchKey(key, username, Role.EMPLOYEE, true);
        if(uname != null){
            Account account = accountRepo.findByUsername(uname);

            EmployeeProfile employeeProfile = employeeProfileRepo.findByAccount_Id(account.getId());
            Hibernate.initialize(employeeProfile.getHotel());

            Account newAccount = new Account();
            newAccount.setRole(Role.EMPLOYEE);
            newAccount.setUsername(username);
            newAccount.setPassword(password);
            newAccount.setActive(true);

            accountRepo.save(newAccount);

            EmployeeProfile newEmployeeProfile = new EmployeeProfile();
            newEmployeeProfile.setAccount(accountRepo.findByUsername(username));
            newEmployeeProfile.setCredentials(credentials);
            newEmployeeProfile.setTelephone(telephone);
            newEmployeeProfile.setHotel(employeeProfile.getHotel());

            employeeProfileRepo.save(newEmployeeProfile);

            return "redirect:/login";
        }

        model.put("username", username);
        model.put("password", password);
        model.put("repeatPassword", repeatPassword);
        model.put("credentials", credentials);
        model.put("telephone", telephone);
        model.put("key", key);

        if (accountRepo.findByUsername(username) != null) {
            model.put("errorUsername", "Данный логин уже занят!");
            return "registrationEmployee";
        }

        model.put("errorKey", "Неверный реферальный код");
        return "registrationEmployee";
    }
}
