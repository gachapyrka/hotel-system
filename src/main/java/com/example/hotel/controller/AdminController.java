package com.example.hotel.controller;

import com.example.hotel.domain.*;
import com.example.hotel.repo.AccountRepo;
import com.example.hotel.repo.EmployeeProfileRepo;
import com.example.hotel.repo.RegistrationKeyRepo;
import com.example.hotel.repo.UserProfileRepo;
import com.example.hotel.service.KeyGenerationService;
import com.example.hotel.service.MailService;
import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    final AccountRepo accountRepo;
    final UserProfileRepo userProfileRepo;

    final EmployeeProfileRepo employeeProfileRepo;
    final RegistrationKeyRepo registrationKeyRepo;
    final KeyGenerationService keyGenerationService;
    final MailService mailService;

    public AdminController(AccountRepo accountRepo, UserProfileRepo userProfileRepo, EmployeeProfileRepo employeeProfileRepo, RegistrationKeyRepo registrationKeyRepo, KeyGenerationService keyGenerationService, MailService mailService) {
        this.accountRepo = accountRepo;
        this.userProfileRepo = userProfileRepo;
        this.employeeProfileRepo = employeeProfileRepo;
        this.registrationKeyRepo = registrationKeyRepo;
        this.keyGenerationService = keyGenerationService;
        this.mailService = mailService;
    }

    @GetMapping("/users")
    public String usersGet(@RequestParam(name = "search", required = false) String search,
                        Map<String, Object> model) {

        if(search == null)
            search = "";

        List<UserProfile> users =
                userProfileRepo.findByAccount_UsernameContainsOrCredentialsContainsOrTelephoneContainsOrPassportContains(search,
                                                                                                                         search,
                                                                                                                         search,
                                                                                                                         search);
        if(!search.equals(""))
            model.put("search", search);

        if(users.size() != 0)
            model.put("users", users);

        return "admin/users";
    }

    @GetMapping("/employees")
    public String employeesGet(@RequestParam(name = "search", required = false) String search,
                           Map<String, Object> model) {

        if(search == null)
            search = "";

        List<EmployeeProfile> employees =
                employeeProfileRepo.findByAccount_UsernameContainsOrCredentialsContainsOrTelephoneContainsOrHotel_NameContains(search,
                        search,
                        search,
                        search);

        for(EmployeeProfile employee: employees){
            Hibernate.initialize(employee.getAccount());
        }

        if(!search.equals(""))
            model.put("search", search);

        if(employees.size() != 0)
            model.put("employees", employees);

        return "admin/employees";
    }

    @PostMapping("/users/newsletter")
    public String usersNewsletterPost(@RequestParam String text, Map<String, Object> model) {

        List<Account> accounts = accountRepo.findByRoleAndActive(Role.USER, true);

        String title = "Сообщение от администрации.";

        for(Account account: accounts){
            mailService.send(title, text, account.getUsername());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/accounts/{id}")
    public String editAccountStatusPost(@PathVariable long id, Model model){
        Account account = accountRepo.findById(id).get();
        if(account!=null){
            account.setActive(!account.isActive());
            accountRepo.save(account);
        }
        if(account.getRole().equals(Role.USER))
            return "redirect:/admin/users";
        else return "redirect:/admin/employees";
    }

    @GetMapping("/message/{id}")
    public String sendMessageGet(@PathVariable long id, Model model){

        Account account = accountRepo.findById(id).get();
        model.addAttribute("account", account);

        return "admin/message";
    }

    @PostMapping("/message/{id}")
    public String sendMessagePost(@PathVariable long id,
                                  @RequestParam String username,
                                  @RequestParam String title,
                                  @RequestParam String text,
                                  Model model){

        Account account = accountRepo.findById(id).get();

        mailService.send(title, text, username);

        if(account.getRole().equals(Role.USER))
            return "redirect:/admin/users";
        else return "redirect:/admin/employees";
    }

    @GetMapping("/registration-keys")
    public String regKeysGet(@RequestParam(name = "username", required = false) String username,
                             Map<String, Object> model) {

        List<RegistrationKey> regKeys = null;
        if(username == null){
            regKeys = registrationKeyRepo.findByRoleAndIsReferal(Role.EMPLOYEE, false);
        }
        else {
            regKeys = registrationKeyRepo.findByUsernameContainingAndRoleAndIsReferal(username, Role.EMPLOYEE, false);
            model.put("searchUsername", username);
        }

        model.put("regKeys", regKeys);

        return "admin/registrationKeys";
    }

    @PostMapping("/registration-keys/regenerate/{id}")
    public String regenerateKeyPost(@PathVariable long id, Model model){
        RegistrationKey registrationKey = registrationKeyRepo.findById(id).get();

        String key = keyGenerationService.generateKey(registrationKey.getUsername(), Role.EMPLOYEE, false, true);

        String title = "Обновленный реферальный код регистрации отеля.";
        String text = "Ваш реферальный код для регистрации отеля был обновлен. Для регистрации введите его в соответствующее поле.\n" +
                 "Новый реферальный код: " + key + "\n";
        text += "Никому не сообщайте и не передавайте данный код во избежание кражи ваших личных данных!";

        mailService.send(title, text, registrationKey.getUsername());

        return "redirect:/admin/registration-keys";
    }

    @PostMapping("/registration-keys/delete/{id}")
    public String deleteKeyPost(@PathVariable long id, Model model){
        RegistrationKey registrationKey = registrationKeyRepo.findById(id).get();

        registrationKeyRepo.deleteById(registrationKey.getId());

        return "redirect:/admin/registration-keys";
    }

    @PostMapping("/registration-keys/generate")
    public String generateKeyPost(@RequestParam String username, Model model){
        RegistrationKey regKey = new RegistrationKey();
        regKey.setReferal(false);
        regKey.setUsername(username);
        regKey.setRole(Role.EMPLOYEE);
        regKey.setKey(keyGenerationService.generateKey(username, Role.EMPLOYEE, false, false));

        String title = "Реферальный код регистрации отеля.";
        String text = "Вам выдан реферальный код для создания аккаунта отеля. Для регистрации введите его в соответствующее поле.\n" +
                "Реферальный код: " + regKey.getKey() + "\n";
        text += "Никому не сообщайте и не передавайте данный код во избежание кражи ваших личных данных!";

        mailService.send(title, text, username);

        return "redirect:/admin/registration-keys";
    }
}
