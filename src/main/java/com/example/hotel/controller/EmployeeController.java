package com.example.hotel.controller;

import com.example.hotel.domain.*;
import com.example.hotel.repo.*;
import com.example.hotel.service.BorrowedRoomService;
import com.example.hotel.service.KeyGenerationService;
import com.example.hotel.service.MailService;
import com.example.hotel.service.QRGenerationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private final BorrowRequestRepo borrowRequestRepo;
    private final BorrowedRoomRepo borrowedRoomRepo;
    private final MailService mailService;
    private final QRGenerationService qrGenerationService;
    private final BorrowedRoomService borrowedRoomService;
    private final RegistrationKeyRepo registrationKeyRepo;
    private final KeyGenerationService keyGenerationService;

    public EmployeeController(EmployeeProfileRepo employeeProfileRepo, HotelRepo hotelRepo, HotelImageRepo hotelImageRepo,
                              RoomTypeRepo roomTypeRepo,
                              RoomImageRepo roomImageRepo,
                              RoomRepo roomRepo, BorrowRequestRepo borrowRequestRepo, BorrowedRoomRepo borrowedRoomRepo, MailService mailService, QRGenerationService qrGenerationService, BorrowedRoomService borrowedRoomService, RegistrationKeyRepo registrationKeyRepo, KeyGenerationService keyGenerationService) {
        this.employeeProfileRepo = employeeProfileRepo;
        this.hotelRepo = hotelRepo;
        this.hotelImageRepo = hotelImageRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.roomImageRepo = roomImageRepo;
        this.roomRepo = roomRepo;
        this.borrowRequestRepo = borrowRequestRepo;
        this.borrowedRoomRepo = borrowedRoomRepo;
        this.mailService = mailService;
        this.qrGenerationService = qrGenerationService;
        this.borrowedRoomService = borrowedRoomService;
        this.registrationKeyRepo = registrationKeyRepo;
        this.keyGenerationService = keyGenerationService;
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

        if(!profile.getHotel().getComments().isEmpty()){
            for(Feedback f: profile.getHotel().getComments()){
                Hibernate.initialize(f.getUserProfile());
            }
            model.put("comments", profile.getHotel().getComments());
        }


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

    @GetMapping("/orders")
    public String ordersGet(@AuthenticationPrincipal Account account,
                            @RequestParam(name = "search", required = false) String search,
                            Map<String, Object> model) {
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hibernate.initialize(profile.getHotel());

        List<BorrowRequest> requests = borrowRequestRepo.findByRoomType_Hotel_Id(profile.getHotel().getId());
        List<BorrowRequest> filtered = new ArrayList<>();
        for(BorrowRequest request: requests){
            Hibernate.initialize(request.getUserProfile());
            Hibernate.initialize(request.getUserProfile().getAccount());
            Hibernate.initialize(request.getRoomType());

            if(search != null){
                if(!(request.getDescription().contains(search) ||
                   request.getUserProfile().getCredentials().contains(search)||
                   request.getUserProfile().getTelephone().contains(search)||
                   request.getUserProfile().getAccount().getUsername().contains(search)||
                   request.getRoomType().getName().contains(search)))
                    continue;
            }

            filtered.add(request);
        }

        if(!filtered.isEmpty())
            model.put("orders", filtered);

        if(search != null)
            model.put("search", search);

        return "employee/orders";
    }

    @GetMapping("/order/{id}/submit")
    public String orderSubmitGet(@PathVariable long id, Map<String, Object> model) {
        BorrowRequest request = borrowRequestRepo.findById(id).get();
        Hibernate.initialize(request.getRoomType());
        Hibernate.initialize(request.getRoomType().getRooms());

        List<Room> rooms = new ArrayList<>();
        for(Room room : request.getRoomType().getRooms()){
            if(!room.isLocked())
                rooms.add(room);
        }
        model.put("rooms", rooms);
        model.put("orderId", id);

        return "employee/submit";
    }

    @PostMapping("/order/{id}/submit")
    public String orderSubmitPost(@PathVariable long id, @RequestParam long roomId, Map<String, Object> model){
        BorrowRequest request = borrowRequestRepo.findById(id).get();
        Hibernate.initialize(request.getUserProfile());
        Hibernate.initialize(request.getUserProfile().getAccount());
        Hibernate.initialize(request.getRoomType());
        Hibernate.initialize(request.getRoomType().getHotel());
        Hotel hotel = request.getRoomType().getHotel();

        Room room = roomRepo.findById(roomId).get();

        BorrowedRoom record = new BorrowedRoom();
        record.setRoom(room);
        record.setStartDate(request.getStartDate());
        record.setEndDate(request.getEndDate());
        record.setUserProfile(request.getUserProfile());
        record.setStatus(Status.ACTIVE);

        borrowedRoomRepo.save(record);
        borrowRequestRepo.deleteById(id);

        String title = "Ваш билет в отеле " + hotel.getName();
        String text = "Для Вас была одобрена бронь номера \"" + room.getNumber() + "\".\n\n";
        text += "Воспользуйтесь QR-кодом, приложенным к сообщению для работы с терминалами дверей и лифтов отеля. За дополнительными инструкциями обращайтесь к сотрудникам.";
        String qr = qrGenerationService.createQR(qrGenerationService.createContent(record), request.getId());

        mailService.send(title, text, request.getUserProfile().getAccount().getUsername(), qr);

        return "redirect:/employee/records";
    }

    @GetMapping("/records")
    public String recordsGet(@AuthenticationPrincipal Account account,
                            @RequestParam(name = "search", required = false) String search,
                            Map<String, Object> model) {
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hibernate.initialize(profile.getHotel());

        List<BorrowedRoom> filtered = borrowedRoomService.getRecordsEmployeeByStatus(profile.getHotel(), search, Status.ACTIVE);

        if(!filtered.isEmpty())
            model.put("records", filtered);

        if(search != null)
            model.put("search", search);

        return "employee/records";
    }

    @PostMapping("/record/{id}")
    public String recordUpdateStatusPost(@PathVariable long id, @RequestParam int status, Map<String, Object> model){
       BorrowedRoom record = borrowedRoomRepo.findById(id).get();
       Status stat = Status.ACTIVE;
       switch (status)
       {
           case 0:{
               stat = Status.ACTIVE;
               break;
           }
           case 1:{
               stat = Status.CANCELLED;
               break;
           }
           case 2:{
               stat = Status.ENDED;
               break;
           }
       }
       record.setStatus(stat);
       record.setEndDate(new Date());
       borrowedRoomRepo.save(record);

        return "redirect:/employee/records";
    }

    @GetMapping("/history")
    public String historyGet(@AuthenticationPrincipal Account account,
                             @RequestParam(name = "search", required = false) String search,
                             Map<String, Object> model) {
        EmployeeProfile profile = employeeProfileRepo.findByAccount_Id(account.getId());
        Hibernate.initialize(profile.getHotel());

        List<BorrowedRoom> filtered = borrowedRoomService.getRecordsEmployeeByStatusNot(profile.getHotel(), search, Status.ACTIVE);

        if(!filtered.isEmpty())
            model.put("records", filtered);

        if(search != null)
            model.put("search", search);

        return "employee/history";
    }

    @GetMapping("/keys")
    public String referalKeysGet(@AuthenticationPrincipal Account account,
                                 Map<String, Object> model) {
        List<RegistrationKey> regKeys =  registrationKeyRepo.findByUsernameContainingAndRoleAndIsReferal(account.getUsername(), Role.EMPLOYEE, true);

        model.put("regKeys", regKeys);

        return "employee/registrationKeys";
    }

    @PostMapping("/keys/regenerate/{id}")
    public String regenerateKeyPost(@PathVariable long id, Model model){
        keyGenerationService.regenerateKey(id);

        return "redirect:/employee/keys";
    }

    @PostMapping("/keys/delete/{id}")
    public String deleteKeyPost(@PathVariable long id, Model model){
        RegistrationKey registrationKey = registrationKeyRepo.findById(id).get();

        registrationKeyRepo.deleteById(registrationKey.getId());

        return "redirect:/employee/keys";
    }

    @PostMapping("/keys/generate")
    public String generateKeyPost(@AuthenticationPrincipal Account account, Model model){
        keyGenerationService.generateKey(account.getUsername(), Role.EMPLOYEE, true, false);

        return "redirect:/employee/keys";
    }
}