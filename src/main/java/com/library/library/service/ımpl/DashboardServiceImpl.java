package com.library.library.service.ımpl;

import com.library.library.dto.DashboardStatsDTO;
import com.library.library.repository.BookRepository;
import com.library.library.repository.CategoryRepository;
import com.library.library.repository.LoansRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoansRepository loansRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalBooks = bookRepository.count();
        long totalUsers = userRepository.count();
        long activeLoans = loansRepository.countByReturnDateIsNull();
        long totalCategories = categoryRepository.count();

        List<Object[]> popularBooksData = loansRepository.findMostPopularBooks();
        List<DashboardStatsDTO.PopularBookDTO> popularBooks = popularBooksData.stream()
                .map(obj -> new DashboardStatsDTO.PopularBookDTO((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        List<Object[]> activeUsersData = loansRepository.findMostActiveUsers();
        List<DashboardStatsDTO.ActiveUserDTO> activeUsers = activeUsersData.stream()
                .map(obj -> new DashboardStatsDTO.ActiveUserDTO((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        return DashboardStatsDTO.builder()
                .totalBooks(totalBooks)
                .totalUsers(totalUsers)
                .activeLoans(activeLoans)
                .totalCategories(totalCategories)
                .popularBooks(popularBooks)
                .activeUsers(activeUsers)
                .build();
    }
}
