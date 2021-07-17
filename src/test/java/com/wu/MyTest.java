package com.wu;

import com.wu.mapper.BillMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: 吴磊
 * @program: smbms-mvc
 * @create: 2021-01-25 12:10
 */
public class MyTest {

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BillMapper billMapper = context.getBean("billMapper", BillMapper.class);
        billMapper.getBillList("", 0, 0).forEach(System.out::println);

    }


}

