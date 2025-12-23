package com.library.library.service;

import com.library.library.dto.DtoReview;
import com.library.library.dto.CreateReviewRequest;
import java.util.List;

public interface ReviewService {
    List<DtoReview> getReviewsByBookId(Long bookId);
    DtoReview createReview(CreateReviewRequest request);
    

    List<DtoReview> getAllReviews();
    void deleteReview(Long reviewId);
}
