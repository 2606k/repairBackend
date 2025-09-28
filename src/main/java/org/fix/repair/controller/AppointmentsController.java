package org.fix.repair.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.fix.repair.common.R;
import org.fix.repair.entity.appointments;
import org.fix.repair.entity.brands;
import org.fix.repair.mapper.AppointmentsMapper;
import org.fix.repair.service.AppointmentsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * 用户预约记录
 */
@RestController
@RequestMapping("/repair/appoint")
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

    @RequestMapping("/list")
    public R<List<appointments>> listAppoint(@RequestBody Map<String, Object> appointInfo){
        if (appointInfo.get("openid") == null){
            Object phone = appointInfo.get("phone");
            Object address = appointInfo.get("address");
            //预约时间
            Object time1 = appointInfo.get("time");
            LambdaQueryWrapper<appointments> queryWrapper = new LambdaQueryWrapper<>();
            // 只有当phone不为空时才添加条件
            if (phone != null && !phone.toString().trim().isEmpty()) {
                queryWrapper.eq(appointments::getPhone, phone);
            }

            // 只有当address不为空时才添加条件
            if (address != null && !address.toString().trim().isEmpty()) {
                queryWrapper.like(appointments::getAddress, address);
            };

            if (time1 != null && !time1.toString().trim().isEmpty()) {

                time1.toString()
                // 将前端传来的日期字符串转换为LocalDate
                LocalDate queryDate = LocalDate.parse(time);

                // 构建日期范围：从当天的00:00:00到23:59:59
                LocalDateTime startTime = queryDate.atStartOfDay();
                LocalDateTime endTime = queryDate.atTime(LocalTime.MAX);
                queryWrapper.between(appointments::getTime, startTime, endTime);
            }
            // 按照时间从早到晚排序（升序）
            queryWrapper.orderByAsc(appointments::getTime);
            return R.ok(appointmentsService.list(queryWrapper));
        }
        LambdaQueryWrapper<appointments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(appointments::getOpenid, appointInfo.get("openid"));
        return R.ok(appointmentsMapper.selectList(queryWrapper));
    }

}
