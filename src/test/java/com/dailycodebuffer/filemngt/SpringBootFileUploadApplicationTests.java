package com.dailycodebuffer.filemngt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class SpringBootFileUploadApplicationTests {

    @Test
    void contextLoads() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());

        String date = String.valueOf(new Date());

        System.out.println(timeStamp);
    }

}
