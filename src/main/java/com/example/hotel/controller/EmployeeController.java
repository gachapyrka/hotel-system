package com.example.hotel.controller;

import com.example.hotel.domain.*;
import com.example.hotel.repo.*;
import com.itextpdf.text.RomanList;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class EmployeeController {
    private final EmployeeProfileRepo employeeProfileRepo;
    private final HotelRepo hotelRepo;
    private final HotelImageRepo hotelImageRepo;
    @Value("${upload.path}")
    private String uploadPath;
    private final RoomTypeRepo roomTypeRepo;
    private final RoomImageRepo roomImageRepo;
    private final RoomRepo roomRepo;

    public EmployeeController(EmployeeProfileRepo employeeProfileRepo, HotelRepo hotelRepo, HotelImageRepo hotelImageRepo,
                              RoomTypeRepo roomTypeRepo,
                              RoomImageRepo roomImageRepo,
                              RoomRepo roomRepo) {
        this.employeeProfileRepo = employeeProfileRepo;
        this.hotelRepo = hotelRepo;
        this.hotelImageRepo = hotelImageRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.roomImageRepo = roomImageRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("/hotel")
    public String hotelGet(@AuthenticationPrincipal Account account, Map<String, Object> model) {
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        Hibernate.initialize(profile.getHotel().getImages());
        Hibernate.initialize(profile.getHotel().getComments());
        Hibernate.initialize(profile.getHotel().getRoomTypes());

        Double minCostPerDay = 0.;
        Double maxCostPerDay = 0.;
        for(RoomType roomType: profile.getHotel().getRoomTypes()){
            if(minCostPerDay == 0 || minCostPerDay > roomType.getCostPerDay())
                minCostPerDay = roomType.getCostPerDay();

            if(maxCostPerDay == 0 || maxCostPerDay < roomType.getCostPerDay())
                maxCostPerDay = roomType.getCostPerDay();
        }

        model.put("account", account);
        model.put("profile", profile);
        model.put("hotel", profile.getHotel());

        if(!profile.getHotel().getImages().isEmpty())
            model.put("hotelImages", profile.getHotel().getImages());

        if(!profile.getHotel().getDescription().isEmpty())
            model.put("description", profile.getHotel().getDescription());

        if(!profile.getHotel().getRoomTypes().isEmpty()){
            for(RoomType roomType: profile.getHotel().getRoomTypes()){
                Hibernate.initialize(roomType.getRooms());
            }
            model.put("roomTypes", profile.getHotel().getRoomTypes());
        }

        if(!profile.getHotel().getComments().isEmpty())
            model.put("comments", profile.getHotel().getComments());

        model.put("minCost", minCostPerDay);
        model.put("maxCost", maxCostPerDay);

        return "employee/hotel";
    }

    @GetMapping("/hotel/edit")
    public String hotelEditGet(@AuthenticationPrincipal Account account, Map<String, Object> model) {
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        model.put("name", profile.getHotel().getName());

        if(!profile.getHotel().getDescription().isEmpty())
            model.put("description", profile.getHotel().getDescription());

        Hibernate.initialize(profile.getHotel().getImages());
        if(!profile.getHotel().getImages().isEmpty())
            model.put("images", profile.getHotel().getImages());

        return "employee/hotelEdit";
    }

    @PostMapping("/hotel/edit")
    public String hotelEditPost(@AuthenticationPrincipal Account account,
                                @RequestParam String name,
                                @RequestParam String description,
                                Map<String, Object> model) {

        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hotel hotel = hotelRepo.findById(profile.getHotel().getId()).get();

        hotel.setName(name);
        hotel.setDescription(description);
        hotelRepo.save(hotel);

        return "redirect:/employee/hotel/edit";
    }

    @PostMapping("/hotel/image/add")
    public String hotelAddImagePost(@AuthenticationPrincipal Account account,
                                @RequestParam MultipartFile file,
                                Map<String, Object> model) throws IOException {

        File uploadDir = new File(uploadPath);


        String uuidFile = UUID.randomUUID().toString();
        String path =  uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + "/" + path));

        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hotel hotel = hotelRepo.findById(profile.getHotel().getId()).get();

        HotelImage hotelImage = new HotelImage();
        hotelImage.setHotel(hotel);
        hotelImage.setPath(path);

        hotelImageRepo.save(hotelImage);

        return "redirect:/employee/hotel/edit";
    }

    @PostMapping("/hotel/image/delete/{id}")
    public String hotelDeleteImagePost(@PathVariable long id, Map<String, Object> model) {

        HotelImage image = hotelImageRepo.findById(id).get();
        File f = new File(uploadPath + "/" +image.getPath());
        f.delete();
        hotelImageRepo.deleteById(id);

        return "redirect:/employee/hotel/edit";
    }

    @PostMapping("/room-type/add")
    public String roomTypeAddPost(@AuthenticationPrincipal Account account, Map<String, Object> model) {

        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hotel hotel = hotelRepo.findById(profile.getHotel().getId()).get();

        RoomType roomType = new RoomType();
        roomType.setName("Без названия");
        roomType.setHotel(hotel);
        roomType.setCostPerDay(0.0);
        roomType.setDescription("");

        roomTypeRepo.save(roomType);

        return "redirect:/employee/hotel";
    }

    @GetMapping("/room-type/{id}")
    public String roomTypeGet(@PathVariable long id, Map<String, Object> model) {
        RoomType roomType = roomTypeRepo.findById(id).get();

        model.put("roomType", roomType);

        Hibernate.initialize(roomType.getRoomImages());
        if(!roomType.getRoomImages().isEmpty())
            model.put("roomImages", roomType.getRoomImages());

        if(!roomType.getDescription().isEmpty())
            model.put("description", roomType.getDescription());

        Hibernate.initialize(roomType.getRooms());
        Hibernate.initialize(roomType.getRequests());
        Hibernate.initialize(roomType.getHotel());

        return "employee/roomType";
    }

    @GetMapping("/room-type/edit/{id}")
    public String roomTypeEditGet(@PathVariable long id, Map<String, Object> model) {
        RoomType roomType = roomTypeRepo.findById(id).get();

        Hibernate.initialize(roomType.getRoomImages());

        model.put("id", roomType.getId());

        if(!roomType.getRoomImages().isEmpty())
            model.put("roomImages", roomType.getRoomImages());

        if(!roomType.getName().isEmpty())
            model.put("name", roomType.getName());

        if(!roomType.getDescription().isEmpty())
            model.put("description", roomType.getDescription());

        model.put("costPerDay", roomType.getCostPerDay());

        Hibernate.initialize(roomType.getRooms());
        Hibernate.initialize(roomType.getRequests());
        Hibernate.initialize(roomType.getHotel());

        return "employee/roomTypeEdit";
    }

    @PostMapping("/room-type/edit/{id}")
    public String roomTypeEditPost(@PathVariable long id,
                                @RequestParam String name,
                                @RequestParam Double costPerDay,
                                @RequestParam String description,
                                Map<String, Object> model) {

        RoomType roomType = roomTypeRepo.findById(id).get();
        roomType.setName(name);
        roomType.setCostPerDay(costPerDay);
        roomType.setDescription(description);
        roomTypeRepo.save(roomType);

        return "redirect:/employee/room-type/edit/" + id;
    }

    @PostMapping("/room-type/delete/{id}")
    public String roomTypeDeletePost(@PathVariable long id, Map<String, Object> model) {
        List<Room> rooms = roomRepo.findByRoomType_Id(id);
        for(Room room: rooms){
            roomRepo.delete(room);
        }
        roomTypeRepo.deleteById(id);

        return "redirect:/employee/hotel";
    }

    @PostMapping("/room-type/{id}/image/add")
    public String roomTypeAddImagePost(@PathVariable long id,
                                    @RequestParam MultipartFile file,
                                    Map<String, Object> model) throws IOException {

        File uploadDir = new File(uploadPath);


        String uuidFile = UUID.randomUUID().toString();
        String path =  uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + "/" + path));

        RoomType roomType = roomTypeRepo.findById(id).get();

        RoomImage roomImage = new RoomImage();
        roomImage.setRoomType(roomType);
        roomImage.setPath(path);

        roomImageRepo.save(roomImage);

        return "redirect:/employee/room-type/edit/" + id;
    }

    @PostMapping("/room-type/image/delete/{id}")
    public String roomTypeDeleteImagePost(@PathVariable long id, Map<String, Object> model) {

        RoomImage image = roomImageRepo.findById(id).get();
        Hibernate.initialize(image.getRoomType());
        File f = new File(uploadPath + "/" +image.getPath());
        f.delete();
        roomImageRepo.deleteById(id);

        return "redirect:/employee/room-type/edit/" + image.getRoomType().getId();
    }

    @GetMapping("/profile")
    public String profileEditGet(@AuthenticationPrincipal Account account, Map<String, Object> model){
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        model.put("profile", profile);

        return "/employee/profile";
    }

    @PostMapping("/profile")
    public String profileEditPost(@AuthenticationPrincipal Account account, @RequestParam String credentials, @RequestParam String telephone, Map<String, Object> model){
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        profile.setCredentials(credentials);
        profile.setTelephone(telephone);

        employeeProfileRepo.save(profile);

        return "redirect:/employee/profile";
    }

    @GetMapping("/rooms")
    public String roomsEditGet(@AuthenticationPrincipal Account account, Map<String, Object> model){
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        Hibernate.initialize(profile.getHotel());
        Hotel hotel = profile.getHotel();
        Hibernate.initialize(hotel.getRoomTypes());

        int count = 0;
        if(!hotel.getRoomTypes().isEmpty()){
            for(RoomType type : hotel.getRoomTypes()){
                Hibernate.initialize(type.getRooms());
                for(Room room : type.getRooms()){
                    Hibernate.initialize(room.getBorrowedRecords());
                    count++;
                }
            }
            model.put("roomTypes", hotel.getRoomTypes());
        }
        model.put("count", count);

        return "/employee/rooms";
    }

    @PostMapping("/room/add")
    public String roomAddPost(@AuthenticationPrincipal Account account, @RequestParam String name, @RequestParam long roomTypeId, Map<String, Object> model){
        RoomType roomType = roomTypeRepo.findById(roomTypeId).get();

        if(roomRepo.findByNumber(name) == null){
            Room room = new Room();
            room.setNumber(name);
            room.setRoomType(roomType);
            roomRepo.save(room);


            return "redirect:/employee/rooms";
        }

        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());

        Hibernate.initialize(profile.getHotel());
        Hotel hotel = profile.getHotel();
        Hibernate.initialize(hotel.getRoomTypes());

        if(!hotel.getRoomTypes().isEmpty()){
            for(RoomType type : hotel.getRoomTypes()){
                Hibernate.initialize(type.getRooms());
                for(Room room : type.getRooms()){
                    Hibernate.initialize(room.getBorrowedRecords());
                }
            }
            model.put("roomTypes", hotel.getRoomTypes());
        }

        model.put("errorText", "Данный номер уже существует!");
        model.put("name", name);

       return "/employee/rooms";
    }

    @PostMapping("/room/delete/{id}")
    public String roomAddPost(@PathVariable long id, Map<String, Object> model){
        roomRepo.deleteById(id);

        return "redirect:/employee/rooms";
    }
}