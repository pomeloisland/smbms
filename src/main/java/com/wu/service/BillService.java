package com.wu.service;

import com.wu.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;




public interface BillService {
    int getBillCountByProviderId(@Param("providerId") int providerId);

    List<Bill> getBillList(String productName,
                           int providerId,
                           int isPayment
    );

    Bill getBillById(int id);

    int modifyBillById(Bill bill);

    int deleteBillById(int id);

    int addBill(Bill bill);
}
