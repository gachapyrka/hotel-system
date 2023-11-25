package com.example.hotel.controller;

import com.example.hotel.domain.*;
import com.example.hotel.repo.*;
import com.example.hotel.service.BorrowedRoomService;
import com.example.hotel.service.MailService;
import com.example.hotel.service.ReportGenerationService;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {
    private final UserProfileRepo userProfileRepo;
    private final BorrowRequestRepo borrowRequestRepo;
    private final BorrowedRoomRepo borrowedRoomRepo;
    private final HotelRepo hotelRepo;
    private final RoomTypeRepo roomTypeRepo;
    private final BorrowedRoomService borrowedRoomService;
    private final ReportGenerationService reportGenerationService;
    private final MailService mailService;
    private final FeedbackRepo feedbackRepo;

    public UserController(UserProfileRepo userProfileRepo, BorrowRequestRepo borrowRequestRepo, BorrowedRoomRepo borrowedRoomRepo, HotelRepo hotelRepo, RoomTypeRepo roomTypeRepo, BorrowedRoomService borrowedRoomService, ReportGenerationService reportGenerationService, MailService mailService,
                          FeedbackRepo feedbackRepo) {
        this.userProfileRepo = userProfileRepo;
        this.borrowRequestRepo = borrowRequestRepo;
        this.borrowedRoomRepo = borrowedRoomRepo;
        this.hotelRepo = hotelRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.borrowedRoomService = borrowedRoomService;
        this.reportGenerationService = reportGenerationService;
        this.mailService = mailService;
        this.feedbackRepo = feedbackRepo;
    }

    @GetMapping("/hotel/{id}/borrow")
    public String borrowGet(@AuthenticationPrincipal Account account, @PathVariable long id, Map<String, Object> model) {
        Hotel hotel = hotelRepo.findById(id).get();

        Hibernate.initialize(hotel.getRoomTypes());
        model.put("roomTypes", hotel.getRoomTypes());
        model.put("hotelId", id);

        return "user/borrow";
    }

    @PostMapping("/hotel/{id}/borrow")
    public String borrowPost(@AuthenticationPrincipal Account account,
                                @PathVariable long id,
                                @RequestParam long roomTypeId,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                @RequestParam String description,
                                Map<String, Object> model) {

        UserProfile profile = userProfileRepo.findByAccount_Id(account.getId());
        Hotel hotel = hotelRepo.findById(id).get();

        BorrowRequest request = new BorrowRequest();
        request.setUserProfile(profile);
        request.setDescription(description);
        request.setRoomType(roomTypeRepo.findById(roomTypeId).get());
        request.setStartDate(new Date());
        request.setCreationDate(new Date());
        request.setEndDate(endDate);

        borrowRequestRepo.save(request);

        return "redirect:/all/hotel/" + id;
    }

    @GetMapping("/orders")
    public String ordersGet(@AuthenticationPrincipal Account account,
                            @RequestParam(name = "search", required = false) String search,
                            Map<String, Object> model) {
        UserProfile profile = userProfileRepo.findByAccount_Id(account.getId());

        List<BorrowRequest> requests = borrowRequestRepo.findByDescriptionContainsOrRoomType_NameContainsOrRoomType_Hotel_NameContains((search == null? "": search), (search == null? "": search), (search == null? "": search));
        List<BorrowRequest> owned = new ArrayList<>();
        for(BorrowRequest request: requests){
            Hibernate.initialize(request.getUserProfile());
            if(request.getUserProfile().getId() == profile.getId()){
                Hibernate.initialize(request.getRoomType());
                Hibernate.initialize(request.getRoomType().getHotel());
                owned.add(request);
            }
        }

        if(!owned.isEmpty())
            model.put("orders", owned);

        if(search != null)
            model.put("search", search);

        return "user/orders";
    }

    @PostMapping("/order/delete/{id}")
    public String orderDeletePost(@PathVariable long id, Map<String, Object> model) {

       borrowRequestRepo.deleteById(id);

       return "redirect:/user/orders";
    }

    @GetMapping("/records")
    public String recordsGet(@AuthenticationPrincipal Account account,
                             @RequestParam(name = "search", required = false) String search,
                             Map<String, Object> model) {
        List<BorrowedRoom> filtered = borrowedRoomService.getRecordsUserByStatus(account, search, Status.ACTIVE);

        if(!filtered.isEmpty())
            model.put("records", filtered);

        if(search != null)
            model.put("search", search);

        return "user/records";
    }

    @GetMapping("/history")
    public String historyGet(@AuthenticationPrincipal Account account,
                             @RequestParam(name = "search", required = false) String search,
                             Map<String, Object> model) {
        List<BorrowedRoom> filtered = borrowedRoomService.getRecordsUserByStatusNot(account, search, Status.ACTIVE);

        if(!filtered.isEmpty())
            model.put("records", filtered);

        if(search != null)
            model.put("search", search);

        return "user/history";
    }

    @GetMapping("/report")
    public RedirectView reportGet(@AuthenticationPrincipal Account account,
                                  @RequestParam(name = "search", required = false) String search,
                                  Map<String, Object> model) {
        List<BorrowedRoom> filtered = borrowedRoomService.getRecordsUserByStatusNot(account, search, Status.ACTIVE);

        String filePath = reportGenerationService.generatePdf(filtered, account.getUsername() + "_" + (new Date()).getTime() + ".pdf");

        String title = "Выписка из истории брони номеров";
        String text = "Файл с таблицей выписки из истории брони номеров на сайте 'Мир Отель'.";

        mailService.send(title, text, account.getUsername(), filePath);

        if(search != null) return new RedirectView("/user/history?search=" + search);
        else return new RedirectView("redirect:/user/history");
    }

    @GetMapping("/comment/add/{id}")
    public String commentGet(@PathVariable long id, Model model){

        Hotel hotel = hotelRepo.findById(id).get();
        model.addAttribute("hotel", hotel);

        return "user/comment";
    }

    @PostMapping("/comment/add/{id}")
    public String sendMessagePost(@AuthenticationPrincipal Account account,
                                  @PathVariable long id,
                                  @RequestParam String title,
                                  @RequestParam String text,
                                  Model model){
        UserProfile profile = userProfileRepo.findByAccount_Id(account.getId());
        Hotel hotel = hotelRepo.findById(id).get();

        Feedback feedback = new Feedback();
        feedback.setUserProfile(profile);
        feedback.setText(text);
        feedback.setTitle(title);
        feedback.setCreationDate(new Date());
        feedback.setHotel(hotel);

        feedbackRepo.save(feedback);

        return "redirect:/all/hotel/" + id;
    }
}
