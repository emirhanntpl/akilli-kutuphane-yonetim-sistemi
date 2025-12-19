package com.library.library.exception;

public enum MessageType {
    NO_RECORD_EXIST("1004","Kayıt Bulunamadı,Lütfen tekrar deneyiniz."),
    TOKEN_IS_VALID("1005","TOKEN SÜRESİ DOLMUŞ"),
    USERNAME_NOT_FOUND("1006","GEÇERSİZ KULLANICI"),
    USERNAME_OR_PASSWORD_INVALID("1007","KULLANICI ADI VEYA ŞİFRE HATALI,TEKRAR DENEYİNİZ."),
    REFRESH_TOKEN_NOT_FOUND("1008","GEÇERSİZ REFRESH TOKEN"),
    REFRESH_TOKEN_IS_VALID("1009","TOKEN SÜRESİ DOLDU"),
    REFRESH_TOKEN_ERROR("1010", "REFRESH TOKEN HATASI"),
    USER_HAS_UNRETURNED_BOOKS("1011", "Kullanıcının iade etmediği kitaplar bulunduğu için silinemez."),
    INVALID_BOOK_NAME("1012","GEÇERSİZ KİTAP ADI LÜTFEN TEKRAR DENEYİNİZ"),
    BOOK_ON_LOAN("1013","KİTAP ÖDünçte olduğu için silinemez, daha sonra tekrar deneyiniz."),
    AUTHOR_NOT_FOUND("1014","YAZAR BULUNAMADI"),
    CATEGORY_NOT_FOUND("1015","KATEGORİ BULUNAMADI"),
    AUTHENTICATION_FAILED("1016","Kimlik doğrulama başarısız oldu."),
    INVALID_AUTHOR_ID("1017","Geçersiz yazar ID'si"),
    INVALID_CATEGORY_ID("1018","Geçersiz kategori ID'si"),
    BOOK_ALREADY_ON_LOAN("1019","Bu kitap zaten başka bir kullanıcı tarafından ödünç alınmış."),
    NO_SUCH_RECORD_FOUND("1020","BÖYLE BİR KAYIT BULUNAMADI."),


    BOOK_NOT_IN_STOCK("1021", "Bu kitap stokta mevcut değil."),
    BOOK_ALREADY_RETURNED("1022", "Bu kitap zaten iade edilmiş."),
    BOOK_IS_IN_STOCK_CANNOT_RESERVE("1023","Kitap stokta varsa rezervasyon yapılamaz"),
    USER_ALREADY_HAS_RESERVATION_FOR_BOOK("1024","Kullanıcının bu kitap için zaten bir rezervasyon var"),

    GENERAL_EXCEPTION("9999","Genel bir hata oluştu,Lütfen tekrar deneyiniz.");
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
