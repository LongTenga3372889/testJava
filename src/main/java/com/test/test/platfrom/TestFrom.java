package com.test.test.platfrom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestFrom {

    Map<String,Integer> map = new HashMap<>();

    @RequestMapping("/{s}/test")
    public void test1(@PathVariable String s){
        for (int i = 0 ;i<100000000;i++) {
            map.put(s,i);
            System.out.println(s+":"+map.get(s));
        }
    }

}
