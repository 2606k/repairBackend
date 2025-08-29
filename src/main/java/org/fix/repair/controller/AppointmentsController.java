package org.fix.repair.controller;

import org.fix.repair.common.R;
import org.fix.repair.mapper.AppointmentsMapper;
import org.fix.repair.service.AppointmentsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户预约记录
 */
@RestController
@RequestMapping("/appoint")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    private AppointmentsMapper appointmentsMapper;

    public AppointmentsController(AppointmentsService appointmentsService, AppointmentsMapper appointmentsMapper) {
        this.appointmentsService = appointmentsService;
        this.appointmentsMapper = appointmentsMapper;
    }

    @PostMapping("/add")
    public R<String> addAppoint(@RequestBody Map<String, Object> appointInfo){
        return appointmentsService.insertAppoint(appointInfo);
    }

}
