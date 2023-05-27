package com.test.pub.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/*接口请求http://localhost:9080/casebk/getImgCode.do*/
@Controller
public class ImgCodeController {
    private final Logger logger = LoggerFactory.getLogger(ImgCodeController.class);
    private final String IMG_CODE = "IMG_CODE";//图片验证码保存Session Key

    private final int width = 82;//图形验证码的宽度
    private final int height = 26;//图形验证码的宽度
    private final int codeCount = 4;//图形验证码中图形个数
//    private GetJSONParam getJSONParam;

    @RequestMapping(value = "/getImgCode.do")
    public void getImgCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(true);
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, height - 2);
        // 设置字体。
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        // 随机产生8条干扰线
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(RandomStringUtils.randomAlphanumeric(1));
            // 产生随机的顏色分量来构造顏色值，这样输出的每位数字的顏色值都将不同。
            red = random.nextInt(155);
            green = random.nextInt(155);
            blue = random.nextInt(155);

            // 用随机产生的顏色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            int x = width / (codeCount + 1);
            g.drawString(strRand, (i + 1) * x, height - 4);

            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        //将图片验证码放到Session中
        session.setAttribute(IMG_CODE, randomCode.toString());
        logger.info("【】session：："+session.getAttribute(IMG_CODE));
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        resp.setContentType("image/jpeg");
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }
    /**
     * 校验图形验证码
     * @throws IOException
     */
    @RequestMapping(value = "/checkImgCode.do")
    @ResponseBody
    public String checkImgCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String imgCodeSec111 = req.getParameter("imgCode");//从前端获取图片验证码
        HttpSession session = req.getSession();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        String imgCodeFir = (String) session.getAttribute(IMG_CODE);//从session中获取图片验证码
        logger.info("【】："+imgCodeFir);
        String imgCodeSec = req.getParameter("imgCode");//从前端获取图片验证码
        if (imgCodeFir == null || StringUtils.isEmpty(imgCodeFir) || imgCodeSec == null || StringUtils.isEmpty(imgCodeSec)){
            logger.info("【checkImgCode-后台获取图形验证码为空】");
            jsonObject.put("returnCode", "0001");
            jsonObject.put("returnInfo", "未获图形验证码");
        } else {
            String a = imgCodeFir.toUpperCase();
            String b = imgCodeSec.toUpperCase();
            if(imgCodeFir.toUpperCase().equals(imgCodeSec.toUpperCase())){
                jsonObject.put("returnCode", "0000");
                jsonObject.put("returnInfo", "图形验证码正确");
            }else{
                logger.info("【/publicReq/checkImgCode-session中验证码与前端获取验证码不匹配】");
                jsonObject.put("returnCode", "0002");
                jsonObject.put("returnInfo", "图形验证码错误");
            }
        }
        String result = jsonObject.toString();

        return result;
    }
}
