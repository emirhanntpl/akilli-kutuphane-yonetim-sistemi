package com.library.library.service;

import com.library.library.model.Announcement;
import java.util.List;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();
    Announcement createAnnouncement(Announcement announcement);
    void deleteAnnouncement(Long id);
}
