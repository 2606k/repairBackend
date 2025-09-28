package org.fix.repair.controller;



import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.fix.repair.common.R;
import org.fix.repair.entity.WeddingUser;
import org.fix.repair.entity.appointments;
import org.fix.repair.mapper.WeddingUserMapper;
import org.fix.repair.mapper.AppointmentsMapper;
import org.fix.repair.service.WeddingUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 用户信息控制器
 */
@Controller
@RequestMapping("/repair/user")
public class WeddingUserController {

    private final WeddingUserMapper userMapper;
    private final WeddingUserService userService;
    private final AppointmentsMapper appointmentsMapper;

    public WeddingUserController(WeddingUserMapper userMapper, WeddingUserService userService, AppointmentsMapper appointmentsMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.appointmentsMapper = appointmentsMapper;
    }


    @PostMapping("/getOpenId")
    @ResponseBody
    public R<String> getOpenId(@RequestBody String json) throws IOException {
        // 解析JSON字符串获取code
        JSONObject jsonObject = JSONObject.parseObject(json);
        String code = jsonObject.getString("code");
        //AppID
        String appId = "wx3194042d59048436";
        //密钥
        String secret= "b71c2c5093cfa11b59f7541b7caede53";
        // 直接使用传入的字符串作为code
        if (StringUtils.isEmpty(code)) {
            return R.error("code不能为空");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId
                +"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";

        //客户端
        OkHttpClient client = new OkHttpClient();
        //用url发起请求
        Request request = new Request.Builder().url(url).build();
        //拿到响应
        Response response = client.newCall(request).execute();
        //如果响应成功，打印返回值
        if (response.isSuccessful()){
            String body = response.body().string();
            System.out.println(body);
            JSONObject jsonObject2 = JSONObject.parseObject(body);
            String openid = jsonObject2.getString("openid");
            return R.ok(openid);
        }
        return R.error("请求失败");
    }


    /**
     * 保存用户信息
     * @param userInfo 用户信息
     * @return 保存结果
     */
    @PostMapping("/register")
    @ResponseBody
    public R<Long> saveUserInfo(@RequestBody Map<String, Object> userInfo) {
        try {
            WeddingUser user = new WeddingUser();
            Object openid = userInfo.get("openid");
            LambdaQueryWrapper<WeddingUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WeddingUser::getOpenid, openid);
            WeddingUser user1= userMapper.selectOne(queryWrapper);
            if (user1 != null){
                user.setUsername((String) userInfo.get("userName"));
                user.setAvatarUrl((String) userInfo.get("avatarUrl"));
                userMapper.update(user, queryWrapper);
                return R.ok(user1.getId());
            }
            user.setUsername((String) userInfo.get("userName"));
            user.setAvatarUrl((String) userInfo.get("avatarUrl"));
//            user.setPhone((String) userInfo.get("phone"));
            user.setOpenid((String) userInfo.get("openid"));
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
     * @param userInfo 登录信息
     * @return 用户信息
     */
    @PostMapping("/checklogin")
    @ResponseBody
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
    /**
     * 根据手机号获取用户信息
     * @param openid 微信唯一id
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    public R<WeddingUser> getUserInfo(@RequestParam String openid) {
        LambdaQueryWrapper<WeddingUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeddingUser::getOpenid, openid);
        WeddingUser user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok(user);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Maps to login.html in templates
    }

    // 后台管理主页
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // 预约记录查看页面
    @GetMapping("/appointments")
    public String appointmentsPage() {
        return "appointments/view";
    }



}