package com.wu.service;

import com.wu.mapper.BillMapper;
import com.wu.pojo.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillMapper billMapper;


    @Override
    public int getBillCountByProviderId(int providerId) {
        return billMapper.getBillCountByProviderId(providerId);
    }

    @Override
    public List<Bill> getBillList(String productName, int providerId, int isPayment) {
        return billMapper.getBillList(productName, providerId, isPayment);
    }

    @Override
    public Bill getBillById(int id) {
        return billMapper.getBillById(id);
    }

    @Override
    public int modifyBillById(Bill bill) {
        return billMapper.modifyBillById(bill);
    }

    @Override
    public int deleteBillById(int id) {
        return billMapper.deleteBillById(id);
    }

    @Override
    public int addBill(Bill bill) {
        return billMapper.addBill(bill);
    }
}
