package com.library.library.controller.Impl;

import com.library.library.controller.RestBaseController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.DashboardStatsDTO;
import com.library.library.service.DashboardService;
import com.library.library.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/dashboard")
public class DashboardControllerImpl extends RestBaseController {

    private final DashboardService dashboardService;
    private final UserService userService;

    public DashboardControllerImpl(DashboardService dashboardService, UserService userService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public RootEntity<DashboardStatsDTO> getDashboardStats() {
        return ok(dashboardService.getDashboardStats());
    }


    @GetMapping("/total-income")
    public RootEntity<Double> getTotalIncome() {
        return ok(userService.getTotalIncome());
    }
}
