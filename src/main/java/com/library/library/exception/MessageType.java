package com.library.library.exception;

public enum MessageType {
    USERNAME_NOT_FOUND("1001", "Kullanıcı adı bulunamadı."),
    NO_RECORD_EXIST("1002", "Böyle bir kayıt mevcut değil."),
    USER_HAS_UNRETURNED_BOOKS("1003", "Kullanıcının iade etmediği kitaplar var."),
    INVALID_BOOK_NAME("1004", "Geçersiz kitap adı."),
    BOOK_NOT_IN_STOCK("1005", "Kitap stokta mevcut değil."),
    NO_SUCH_RECORD_FOUND("1006", "Böyle bir kayıt bulunamadı."),
    BOOK_ALREADY_RETURNED("1007", "Kitap zaten iade edilmiş."),
    AUTHENTICATION_FAILED("1008", "Kimlik doğrulama başarısız."),
    REFRESH_TOKEN_NOT_FOUND("1009", "Yenileme token'ı bulunamadı."),
    REFRESH_TOKEN_IS_VALID("1010", "Yenileme token'ı geçerli değil."),
    BOOK_IS_IN_STOCK_CANNOT_RESERVE("1011", "Kitap stokta olduğu için rezerve edilemez."),
    USER_ALREADY_HAS_RESERVATION_FOR_BOOK("1012", "Bu kitap için zaten aktif bir rezervasyonunuz var."),
    AUTHOR_NOT_FOUND("1013", "Yazar bulunamadı."),
    CATEGORY_NOT_FOUND("1014", "Kategori bulunamadı."),
    INVALID_AUTHOR_ID("1015", "Geçersiz yazar ID'si."),
    BOOK_ON_LOAN("1016", "Kitap şu anda ödünçte olduğu için silinemez."),
    BOOK_IS_RESERVED("1017", "Kitap rezerve edildiği için silinemez."),
    INVALID_CATEGORY_ID("1018", "Geçersiz kategori ID'si."); // YENİ EKLENDİ

    private final String code;
    private final String message;

    MessageType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
