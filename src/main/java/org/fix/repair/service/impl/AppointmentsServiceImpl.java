package org.fix.repair.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fix.repair.common.R;
import org.fix.repair.entity.appointments;
import org.fix.repair.mapper.AppointmentsMapper;
import org.fix.repair.service.AppointmentsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AppointmentsServiceImpl extends ServiceImpl <AppointmentsMapper, appointments> implements AppointmentsService {


    @Override
    public R<String> insertAppoint(Map<String, Object> appointInfo) {
        appointments appoint = new appointments();
        appoint.setOpenid((String) appointInfo.get("openid"));
        appoint.setName((Long) appointInfo.get("name"));
        appoint.setPhone((String) appointInfo.get("phone"));
        appoint.setTime((Date) appointInfo.get("time"));
        appoint.setAddress((String) appointInfo.get("address"));
        appoint.setStatus((String) appointInfo.get("status"));
        appoint.setBrandname((Long) appointInfo.get("brandname"));
        appoint.setRepairtype((Long) appointInfo.get("repairtype"));
        appoint.setCreatedat(new java.util.Date());
        boolean save = this.save(appoint);
        if (!save) {
            return R.error("保存失败");
        }
        return R.ok("保存成功");
    }
}
