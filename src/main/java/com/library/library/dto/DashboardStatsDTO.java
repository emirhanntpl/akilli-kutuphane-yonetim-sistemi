package com.library.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalBooks;
    private long totalUsers;
    private long activeLoans;
    private long totalCategories;
    private List<PopularBookDTO> popularBooks;
    private List<ActiveUserDTO> activeUsers;

    @Data
    @AllArgsConstructor
    public static class PopularBookDTO {
        private String title;
        private long count;
    }

    @Data
    @AllArgsConstructor
    public static class ActiveUserDTO {
        private String username;
        private long count;
    }
}
