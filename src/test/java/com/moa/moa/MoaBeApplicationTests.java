package com.moa.moa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MoaBeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void t1() {
        System.out.println("test sysout");

        for (int i = 0; i < 100; i++) {
            System.out.println("test i : " + i);
        }
    }

}
