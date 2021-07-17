package com.wu.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.wu.pojo.Provider;
import com.wu.pojo.User;
import com.wu.service.ProviderServiceImpl;
import com.wu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/jsp")
public class ProviderController {

    @Autowired
    private ProviderServiceImpl providerService;

    @RequestMapping("/providerManagement")
    public String toProviderManagementPage(String queryProName, String queryProCode, Model model) {
        if (StringUtils.isNullOrEmpty(queryProName)) {
            queryProName = "";
        }
        if (StringUtils.isNullOrEmpty(queryProCode)) {
            queryProCode = "";
        }
        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);

        return "WEB-INF/jsp/providerlist";
    }


    @RequestMapping("/viewprovider")
    public String toViewProviderPage(String proid, Model model) {
        if (!StringUtils.isNullOrEmpty(proid)) {
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            model.addAttribute("provider", provider);
        }
        return "WEB-INF/jsp/providerview";
    }

    @RequestMapping("/modifyprovide")
    public String toModifyPage(String proid, Model model) {
        if (!StringUtils.isNullOrEmpty(proid)) {
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            model.addAttribute("provider", provider);
        }
        return "WEB-INF/jsp/providermodify";
    }

    @RequestMapping("/modifyprosave")
    public String modifyPro(Provider provider, HttpSession session) {
        System.out.println(provider);
        provider.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        int i = providerService.modifyProviderById(provider);
        if (i > 0) {
            return "redirect:/jsp/providerManagement";
        } else {
            return "WEB-INF/jsp/providermodify";
        }
    }

    @RequestMapping("/deletepro")
    @ResponseBody
    public String deleteProvider(String id) {
        HashMap<String, String> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(id)) {
            int flag = providerService.deleteProviderById(Integer.parseInt(id));
            if (flag == 0) {//删除成功
                resultMap.put("delResult", "true");
            } else if (flag == -1) {//删除失败
                resultMap.put("delResult", "false");
            } else if (flag > 0) {//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/toaddprovider")
    public String toAddProviderPage() {
        return "WEB-INF/jsp/provideradd";
    }

    @RequestMapping("/addprovider")
    public String addProvider(Provider provider, HttpSession session) {
        provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        int i = providerService.addProvider(provider);
        if (i > 0) {
            return "redirect:/jsp/providerManagement";
        } else {
            return "WEB-INF/jsp/provideradd";
        }
    }

    @RequestMapping("/getproviderlist")
    @ResponseBody
    public String getProviderlist() {
        List<Provider> providerList = providerService.getProviderList("", "");
        return JSONArray.toJSONString(providerList);
    }
}
