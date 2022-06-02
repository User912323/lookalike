package com.dailycodebuffer.filemngt;

import Luxand.FSDK;
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


        try {
            int r = FSDK.ActivateLibrary("CrPcEyeks2cem4x7Vhs+3859Hamx2YPBj1LZTj4GTdKCerk2ljpdT2bLm/TR7NP173eTkECFRuXEJ4LqCP659uxZJ9Ahga15xa8TrcoHEEgfO73TGNlIRxp9us+nMEFp7BJyBRE48VcMo/UtCdEpXv7lg/zvvw+z6h0vizhyeWs=");
            if (r != FSDK.FSDKE_OK) {
                System.out.println("error1");
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("error2");
        }

        System.out.println("sukses");

        FSDK.Initialize();


    }

    }


