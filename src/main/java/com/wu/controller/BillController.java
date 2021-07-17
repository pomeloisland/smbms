package com.wu.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.wu.pojo.Bill;
import com.wu.pojo.Provider;
import com.wu.pojo.User;
import com.wu.service.BillServiceImpl;
import com.wu.service.ProviderServiceImpl;
import com.wu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/jsp")
public class BillController {

    @Autowired
    private ProviderServiceImpl providerService;


    @Autowired
    private BillServiceImpl billService;


    @RequestMapping("/billManagement")
    public String getBillList(Model model, String queryProductName,
                              String queryProviderId,
                              String queryIsPayment
    ) {
        List<Provider> providerList = providerService.getProviderList("", "");
        model.addAttribute("providerList", providerList);
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }
        int ProviderId = 0;
        if (!StringUtils.isNullOrEmpty(queryProviderId)) {
            ProviderId = Integer.parseInt(queryProviderId);
        }
        int IsPayment = 0;
        if (!StringUtils.isNullOrEmpty(queryIsPayment)) {
            IsPayment = Integer.parseInt(queryIsPayment);
        }
        List<Bill> billList = billService.getBillList(queryProductName, ProviderId, IsPayment);

        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", ProviderId);
        model.addAttribute("queryIsPayment", IsPayment);
        return "WEB-INF/jsp/billlist";
    }


    @RequestMapping("/addbillpage")
    public String toAddBillPage() {
        return "WEB-INF/jsp/billadd";
    }

    @RequestMapping("/viewbill")
    public String toViewPage(String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(Integer.parseInt(id));
            model.addAttribute("bill", bill);
        }
        return "WEB-INF/jsp/billview";
    }

    @RequestMapping("/modifybill")
    public String modifyBill(String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(Integer.parseInt(id));
            model.addAttribute("bill", bill);
        }
        return "WEB-INF/jsp/billmodify";
    }

    @RequestMapping("/savebill")
    public String saveModifyBill(Bill bill, HttpSession session) {

        bill.setProductCount(new BigDecimal(String.valueOf(bill.getProductCount())).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(String.valueOf(bill.getTotalPrice())).setScale(2, BigDecimal.ROUND_DOWN));

        bill.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());

        int i = billService.modifyBillById(bill);
        if (i > 0) {
            return "redirect:/jsp/billManagement";
        } else {
            return "WEB-INF/jsp/billmodify";
        }
    }

    @RequestMapping("/delbill")
    @ResponseBody
    public String deleteBillById(String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            int i = billService.deleteBillById(Integer.parseInt(id));
            if (i > 0) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/addbill")
    public String addBill(Bill bill, HttpSession session) {
        bill.setProductCount(new BigDecimal(String.valueOf(bill.getProductCount())).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(String.valueOf(bill.getTotalPrice())).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        int i = billService.addBill(bill);
        if (i > 0) {
            return "redirect:/jsp/billManagement";
        } else {
            return "WEN-INF/jsp/billadd";
        }
    }

}
