package com.example.hotel.service;

import com.example.hotel.domain.Account;
import com.example.hotel.domain.RegistrationKey;
import com.example.hotel.domain.Role;
import com.example.hotel.repo.RegistrationKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class KeyGenerationService {

    private final RegistrationKeyRepo registrationKeyRepo;

    public KeyGenerationService(RegistrationKeyRepo registrationKeyRepo) {
        this.registrationKeyRepo = registrationKeyRepo;
    }

    private String getKey(){
        Random random = new Random((new Date().getTime()));
        String str = "";
        for(int i=0; i<16; ++i){
            str += (char)(Math.abs(random.nextInt()%26) + (random.nextInt()%2==0?  + 'a' : 'A'));

        }

        return str;
    }
    public String generateKey(String username, Role role, boolean isReferal){
        String key = getKey();
        List<RegistrationKey> registrationKeys = registrationKeyRepo.findByUsername(username);

        boolean isFound = false;
        for(RegistrationKey registrationKey : registrationKeys){
            if(registrationKey.getUsername().equals(username) &&
                       registrationKey.getRole().equals(role) &&
                       registrationKey.isReferal() == isReferal){
                isFound = true;
                registrationKey.setKey(key);
                registrationKeyRepo.save(registrationKey);
                break;
            }
        }

        if(!isFound){
            RegistrationKey newKey = new RegistrationKey();
            newKey.setKey(key);
            newKey.setUsername(username);
            newKey.setRole(role);
            newKey.setReferal(isReferal);
            registrationKeyRepo.save(newKey);
        }

        return key;
    }
    public String matchKey(String key, String username, Role role, boolean isReferal){
        List<RegistrationKey> registrationKeys = registrationKeyRepo.findByUsername(username);
        for(RegistrationKey registrationKey : registrationKeys){
            if(registrationKey.getUsername().equals(username) &&
                       registrationKey.getRole().equals(role) &&
                       registrationKey.isReferal() == isReferal &&
                        registrationKey.getKey().equals(key)){
                registrationKeyRepo.deleteById(registrationKey.getId());
                return registrationKey.getUsername();
            }
        }

        return null;
    }
}
