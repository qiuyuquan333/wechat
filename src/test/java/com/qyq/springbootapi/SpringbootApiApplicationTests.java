package com.qyq.springbootapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApiApplicationTests {


    @Test
    public void contextLoads(){
        Map<String,Object> map = new HashMap();
        map.put("name","张三");
        map.put("age",18);
        map.put("sex","女");

//        for (Object o : map.keySet()) {
//            System.out.println("key="+o+",value="+map.get(o));
//        }

//        Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, Object> next = iterator.next();
//            System.out.println(next.getKey()+",value="+next.getValue());
//        }

//        for (Entry<String,Object> entry:  map.entrySet()){
//            System.out.println(entry.getKey()+","+entry.getValue());
//        }

        for (Object o : map.values()){
            System.out.println(o);
        }

    }


}
