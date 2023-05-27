package com.test.silder_verify.controller;

import com.test.silder_verify.entity.Captcha;
import com.test.silder_verify.entity.LoginUser;
import com.test.silder_verify.utils.PuzzleCaptchaUtil;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.*;

/**
 * 滑块验证码案例
 * 前端代码：D:\VueDemo\silder-verify-vue
 */
@RestController
@RequestMapping("/silderVerify")
public class SilderVerifyController {

    //拼图验证码允许偏差
    private static Integer ALLOW_DEVIATION = 3;

    @CrossOrigin
    @PostMapping("getCaptcha")
    public Object getCaptcha(@RequestBody Captcha captcha) {
        return PuzzleCaptchaUtil.getCaptcha(captcha);
    }

    @CrossOrigin
    @PostMapping("login")
    public Object login(@RequestBody LoginUser loginUser) {
        //校验验证码是否存在
        if (!PuzzleCaptchaUtil.CACHE_CODE.containsKey(loginUser.getNonceStr())) {
            return "验证码已失效";
        }
        //获取缓存验证码
        Integer value = PuzzleCaptchaUtil.CACHE_CODE.get(loginUser.getNonceStr());
        //根据移动距离判断验证是否成功
        if (Math.abs(value - Integer.parseInt(loginUser.getValue())) > ALLOW_DEVIATION) {
            return "验证码有误，请重试";
        }
        return "ok";
    }

}
