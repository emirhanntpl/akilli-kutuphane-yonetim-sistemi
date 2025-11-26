package com.library.library.exception;

import lombok.Getter;

@Getter
public enum MessageType {

    NO_RECORD_EXIST("1004","Kayıt Bulunamadı,Lütfen tekrar deneyiniz."),
    TOKEN_IS_VALID("1005","TOKEN SÜRESİ DOLMUŞ"),
    USERNAME_NOT_FOUND("1006","GEÇERSİZ KULLANICI"),
    USERNAME_OR_PASSWORD_INVALID("1007","KULLANICI ADI VEYA ŞİFRE HATALI,TEKRAR DENEYİNİZ."),
    REFRESH_TOKEN_NOT_FOUND("1008","GEÇERSİZ REFRESH TOKEN"),
    REFRESH_TOKEN_IS_VALID("1009","TOKEN SÜRESİ DOLDU"),
    REFRESH_TOKEN_ERROR("1010", "REFRESH TOKEN HATASI"), // Yeni eklenen hata
    GENERAL_EXCEPTION("9999","Genel bir hata oluştu,Lütfen tekrar deneyiniz.");

    private final String code;
    private final String message;

    MessageType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
