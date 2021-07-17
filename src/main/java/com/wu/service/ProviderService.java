package com.wu.service;

import com.wu.pojo.Provider;

import java.util.List;


public interface ProviderService {
    List<Provider> getProviderList(String proName, String proCode);

    Provider getProviderById(int id);

    int modifyProviderById(Provider provider);

    int deleteProviderById(int id);

    int addProvider(Provider provider);
}
