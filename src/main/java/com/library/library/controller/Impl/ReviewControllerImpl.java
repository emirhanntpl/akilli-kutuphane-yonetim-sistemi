package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.CreateReviewRequest;
import com.library.library.dto.DtoReview;
import com.library.library.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/reviews")
@AllArgsConstructor
public class ReviewControllerImpl extends RestBaseController {

    private final ReviewService reviewService;


    @GetMapping
    public RootEntity<List<DtoReview>> getAllReviews() {
        return ok(reviewService.getAllReviews());
    }

    @GetMapping("/book/{bookId}")
    public RootEntity<List<DtoReview>> getReviewsByBookId(@PathVariable Long bookId) {
        return ok(reviewService.getReviewsByBookId(bookId));
    }

    @PostMapping
    public RootEntity<DtoReview> createReview(@RequestBody @Valid CreateReviewRequest request) {
        return ok(reviewService.createReview(request));
    }


    @DeleteMapping("/{id}")
    public RootEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ok("Yorum başarıyla silindi.");
    }
}
