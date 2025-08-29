package org.fix.repair.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.fix.repair.common.R;
import org.fix.repair.entity.WeddingUser;
import org.fix.repair.mapper.WeddingUserMapper;
import org.fix.repair.service.WeddingUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户信息控制器
 */
@RestController
@RequestMapping("/user")
public class WeddingUserController {

    private final WeddingUserMapper userMapper;

    private final WeddingUserService userService;

    public WeddingUserController(WeddingUserMapper userMapper, WeddingUserService userService) {
        this.userService = userService;
        this.userMapper = userMapper;
    }



    /**
     * 保存用户信息
     * @param userInfo 用户信息
     * @return 保存结果
     */
    @PostMapping("/register")
    public R<Long> saveUserInfo(@RequestBody Map<String, Object> userInfo) {
        try {
            WeddingUser user = new WeddingUser();
            user.setUsername((String) userInfo.get("userName"));
            user.setAvatarUrl((String) userInfo.get("avatarUrl"));
            user.setPhone((String) userInfo.get("phone"));
            user.setPassword((String) userInfo.get("password"));
            user.setCreatedat(new java.util.Date());
            userMapper.insert(user);
            return R.ok(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录
     * @param
     * @return 用户信息
     */
    @PostMapping("/login")
    public R<WeddingUser> login(@RequestBody Map<String, Object> userInfo) {
        try {
            LambdaQueryWrapper<WeddingUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WeddingUser::getPhone, userInfo.get("phone"))
                    .eq(WeddingUser::getPassword, userInfo.get("password"));
            WeddingUser user= userMapper.selectOne(queryWrapper);
            if (user == null) {
                return R.error("用户不存在");
            }
            return R.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("用户登录失败: " + e.getMessage());
        }
    }



}