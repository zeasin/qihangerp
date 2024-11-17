package cn.qihangerp.api.controller;

//import com.alibaba.nacos.api.config.annotation.NacosValue;
import cn.qihangerp.common.config.QiHangErpConfig;
import cn.qihangerp.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
//    @Autowired
//    private MySpiderController spiderController;
    /** 系统基础配置 */
    @Autowired
    private QiHangErpConfig config;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", config.getName(), config.getVersion());
    }

//    @GetMapping("/test/22")
//    public String home(){
//        spiderController.startSpider();
//        return "hello world";
//    }

    @GetMapping("/home")
    public String home(String code){

        return code;
    }
    @Value(value = "${zhijian.name:1}")
    private String serverName;

    @GetMapping(value = "/test/na")
    public String get() {
        return serverName;
    }

}
