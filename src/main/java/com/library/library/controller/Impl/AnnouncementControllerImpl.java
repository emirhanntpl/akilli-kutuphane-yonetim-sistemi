package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.model.Announcement;
import com.library.library.service.AnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/announcements")
@AllArgsConstructor
public class AnnouncementControllerImpl extends RestBaseController {

    private final AnnouncementService announcementService;

    @GetMapping
    public RootEntity<List<Announcement>> getAllAnnouncements() {
        return ok(announcementService.getAllAnnouncements());
    }

    @PostMapping
    public RootEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        return ok(announcementService.createAnnouncement(announcement));
    }

    @DeleteMapping("/{id}")
    public RootEntity<String> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ok("Duyuru başarıyla silindi.");
    }
}
