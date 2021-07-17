package com.wu.service;

import com.wu.mapper.BillMapper;
import com.wu.mapper.ProviderMapper;
import com.wu.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProviderServiceImpl implements ProviderMapper {

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private BillMapper billMapper;


    @Override
    public List<Provider> getProviderList(String proName, String proCode) {
        return providerMapper.getProviderList(proName, proCode);
    }

    @Override
    public Provider getProviderById(int id) {
        return providerMapper.getProviderById(id);
    }

    @Override
    public int modifyProviderById(Provider provider) {
        return providerMapper.modifyProviderById(provider);
    }

    @Override
    public int deleteProviderById(int id) {
        //删除供应商前先查询订单里面有没有订单
        int billCount = -1;
        billCount = billMapper.getBillCountByProviderId(id);
        if (billCount == 0) {
            try {
                providerMapper.deleteProviderById(id);
            } catch (Exception e) {
                e.printStackTrace();
                billCount = -1;
            }
        }
        //返回的订单数
        return billCount;
    }

    @Override
    public int addProvider(Provider provider) {
        return providerMapper.addProvider(provider);
    }
}
