package mblog.web.controller.desk;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mblog.base.data.DataExt;
import mblog.base.email.EmailSender;
import mblog.base.lang.Consts;
import mblog.core.data.AccountProfile;
import mblog.core.data.User;
import mblog.core.persist.service.UserService;
import mblog.core.persist.service.VerifyService;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Data;

/**
 * @author cdq2016/8/14.
 */
@Controller
@RequestMapping("/email")
public class EmailController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private EmailSender emailSender;

    @RequestMapping(value = "/retry/{userId}", method = RequestMethod.GET)
    public String retry(@PathVariable("userId") Long userId, Integer type, ModelMap model) {
        Assert.notNull(userId, "缺少必要的参数");
        Assert.notNull(type, "缺少必要的参数");

        String template = Consts.EMAIL_TEMPLATE_BIND;
        String subject = "邮箱绑定验证";

        if (type == Consts.VERIFY_FORGOT) {
            template = Consts.EMAIL_TEMPLATE_FORGOT;
            subject = "找回密码";
        }

        User user = userService.get(userId);

        String code = verifyService.generateCode(user.getId(), type, user.getEmail());
        Map<String, Object> context = new HashMap<>();
        context.put("userId", user.getId());
        context.put("code", code);

        emailSender.sendTemplete(user.getEmail(), subject, template, context);

        DataExt data = DataExt.success("邮件发送成功", Data.NOOP);
        model.put("data", data);

        data.addLink("email/retry/" + userId + "?type=" + type, "没收到? 再来一发");

        return getView(Views.REG_RESULT);
    }

    @RequestMapping(value = "/bind/{userId}", method = RequestMethod.GET)
    public String bind(@PathVariable("userId") Long userId, Integer type, String code, ModelMap model) {
        Assert.notNull(userId, "缺少必要的参数");
        DataExt data;
        try {
            verifyService.verify(userId, Consts.VERIFY_BIND, code);
            AccountProfile p = userService.updateActiveEmail(userId, Consts.ACTIVE_EMAIL);

            putProfile(p);

            data = DataExt.success("恭喜您! 邮箱绑定成功。", Data.NOOP);

            data.addLink("index", "返回首页");
        } catch (Exception e) {
            data = DataExt.failure(e.getMessage());

            // data.addLink("email/retry/" + userId + "?type=" +type, "重发邮件");
        }

        model.put("type", type);
        model.put("userId", userId);
        model.put("data", data);
        return getView(Views.REG_RESULT);
    }

    @RequestMapping(value = "/forgot/{userId}", method = RequestMethod.GET)
    public String forgot(@PathVariable("userId") Long userId, Integer type, String code, ModelMap model) {
        Assert.notNull(userId, "缺少必要的参数");

        model.put("userId", userId);
        DataExt data;
        try {
            String token = verifyService.verify(userId, Consts.VERIFY_FORGOT, code);
            model.put("token", token);

            return getView(Views.FORGOT_RESET);
        } catch (Exception e) {
            data = DataExt.failure(e.getMessage());

            // data.addLink("email/retry/" + userId + "?type=" +type, "重发邮件");
        }

        model.put("type", type);
        model.put("data", data);
        return getView(Views.REG_RESULT);
    }

}
