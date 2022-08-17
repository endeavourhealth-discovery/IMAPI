package org.endeavourhealth.imapi.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// Standard (RFC 7807) REST API (error) response structure
// https://www.rfc-editor.org/rfc/rfc7807
public class ProblemDetailResponse {
    public static final MediaType JSON_MEDIA_TYPE =
        MediaType.valueOf("application/problem+json");

    public static ResponseEntity<ProblemDetailResponse> create(HttpStatus status) {
        ProblemDetailResponse pdr = new ProblemDetailResponse().setStatus(status.value());
        return new ResponseEntity<>(pdr, status);
    }

    public static ResponseEntity<ProblemDetailResponse> create(HttpStatus status, String title) {
        ProblemDetailResponse pdr = new ProblemDetailResponse()
            .setStatus(status.value())
            .setTitle(title);
        return new ResponseEntity<>(pdr, status);
    }

    public static ResponseEntity<ProblemDetailResponse> create(HttpStatus status, String title, String detail) {
        ProblemDetailResponse pdr = new ProblemDetailResponse()
            .setStatus(status.value())
            .setTitle(title)
            .setDetail(detail);
        return new ResponseEntity<>(pdr, status);
    }

    private Integer status;
    private String title;
    private String detail;

    public Integer getStatus() {
        return status;
    }

    public ProblemDetailResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProblemDetailResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public ProblemDetailResponse setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
