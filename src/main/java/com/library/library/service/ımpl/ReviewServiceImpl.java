package com.library.library.service.ımpl;

import com.library.library.dto.CreateReviewRequest;
import com.library.library.dto.DtoReview;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.model.Book;
import com.library.library.model.Review;
import com.library.library.model.User;
import com.library.library.repository.BookRepository;
import com.library.library.repository.ReviewRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public List<DtoReview> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DtoReview createReview(CreateReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_BOOK_NAME, HttpStatus.NOT_FOUND));

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    // YENİ EKLENEN METOTLAR
    @Override
    public List<DtoReview> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private DtoReview convertToDto(Review review) {
        return new DtoReview(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUser().getUsername(),
                review.getCreateTime()
        );
    }
}
