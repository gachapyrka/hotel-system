package com.example.hotel.service;

import com.example.hotel.domain.Account;
import com.example.hotel.domain.BorrowedRoom;
import com.example.hotel.domain.Hotel;
import com.example.hotel.domain.Status;
import com.example.hotel.repo.BorrowedRoomRepo;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowedRoomService {
    private final BorrowedRoomRepo borrowedRoomRepo;

    public BorrowedRoomService(BorrowedRoomRepo borrowedRoomRepo) {
        this.borrowedRoomRepo = borrowedRoomRepo;
    }

    private boolean checkAndInitUser(BorrowedRoom record, String search){
        Hibernate.initialize(record.getRoom());
        Hibernate.initialize(record.getRoom().getRoomType());
        Hibernate.initialize(record.getRoom().getRoomType().getHotel());

        if(search == null)
            return true;

        return record.getRoom().getRoomType().getHotel().getName().contains(search) ||
                record.getRoom().getRoomType().getName().contains(search)||
                record.getRoom().getNumber().contains(search);
    }

    public List<BorrowedRoom> getRecordsUserByStatus(Account account, String search, Status status){
        List<BorrowedRoom> records = borrowedRoomRepo.findByUserProfile_Account_Id(account.getId());
        List<BorrowedRoom> filtered = new ArrayList<>();
        for(BorrowedRoom record: records){

            if(record.getStatus() != status)
                continue;

            if(!checkAndInitUser(record, search))
                continue;

            filtered.add(record);
        }

        return filtered;
    }

    public List<BorrowedRoom> getRecordsUserByStatusNot(Account account, String search, Status status){
        List<BorrowedRoom> records = borrowedRoomRepo.findByUserProfile_Account_Id(account.getId());
        List<BorrowedRoom> filtered = new ArrayList<>();
        for(BorrowedRoom record: records){

            if(record.getStatus() == status)
                continue;

            if(!checkAndInitUser(record, search))
                continue;

            filtered.add(record);
        }

        return filtered;
    }

    private boolean checkAndInitEmployee(BorrowedRoom record, String search){
        Hibernate.initialize(record.getUserProfile());
        Hibernate.initialize(record.getUserProfile().getAccount());
        Hibernate.initialize(record.getRoom());

        if(search == null)
            return true;

        return record.getUserProfile().getAccount().getUsername().contains(search) ||
                record.getUserProfile().getCredentials().contains(search)||
                record.getUserProfile().getTelephone().contains(search)||
                record.getRoom().getNumber().contains(search);
    }

    public List<BorrowedRoom> getRecordsEmployeeByStatus(Hotel hotel, String search, Status status){
        List<BorrowedRoom> records = borrowedRoomRepo.findByRoom_RoomType_Hotel_Id(hotel.getId());
        List<BorrowedRoom> filtered = new ArrayList<>();
        for(BorrowedRoom record: records){

            if(record.getStatus() != status)
                continue;

            if(!checkAndInitEmployee(record, search))
                continue;

            filtered.add(record);
        }

        return filtered;
    }

    public List<BorrowedRoom> getRecordsEmployeeByStatusNot(Hotel hotel, String search, Status status){
        List<BorrowedRoom> records = borrowedRoomRepo.findByRoom_RoomType_Hotel_Id(hotel.getId());
        List<BorrowedRoom> filtered = new ArrayList<>();
        for(BorrowedRoom record: records){

            if(record.getStatus() == status)
                continue;

            if(!checkAndInitEmployee(record, search))
                continue;

            filtered.add(record);
        }

        return filtered;
    }
}
