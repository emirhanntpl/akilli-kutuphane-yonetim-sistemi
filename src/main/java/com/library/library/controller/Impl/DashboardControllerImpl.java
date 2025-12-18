package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.repository.BookRepository;
import com.library.library.repository.CategoryRepository;
import com.library.library.repository.LoansRepository;
import com.library.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/api/dashboard")
@AllArgsConstructor
public class DashboardControllerImpl extends RestBaseController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoansRepository loansRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/stats")
    public RootEntity<Map<String, Object>> getStats() {
        long totalBooks = bookRepository.count();
        long totalUsers = userRepository.count();
        long activeLoans = loansRepository.countByReturnDateIsNull();
        long totalCategories = categoryRepository.count();

        List<Map<String, Object>> popularBooks = loansRepository.findMostPopularBooks().stream()
                .map(result -> Map.of("title", result[0], "count", result[1]))
                .collect(Collectors.toList());

        List<Map<String, Object>> activeUsers = loansRepository.findMostActiveUsers().stream()
                .map(result -> Map.of("username", result[0], "count", result[1]))
                .collect(Collectors.toList());

        Map<String, Object> stats = Map.of(
                "totalBooks", totalBooks,
                "totalUsers", totalUsers,
                "activeLoans", activeLoans,
                "totalCategories", totalCategories,
                "popularBooks", popularBooks,
                "activeUsers", activeUsers
        );

        return ok(stats);
    }
}
