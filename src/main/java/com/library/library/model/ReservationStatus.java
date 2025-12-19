package com.library.library.model;

public enum ReservationStatus {
    WAITING,    // Sırada bekliyor
    NOTIFIED,   // Kitap geldi, kullanıcıya haber verildi
    COMPLETED,  // Kullanıcı kitabı aldı
    CANCELLED   // İptal edildi
}
