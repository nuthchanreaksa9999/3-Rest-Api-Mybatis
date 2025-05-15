package co.istad.mobilebanking;

import co.istad.mobilebanking.api.auth.AuthMapper;
import co.istad.mobilebanking.api.auth.AuthProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MobileBankingApplicationTests {

    @Autowired
    AuthMapper authMapper;

    @Test
    void test() {

        System.out.println(authMapper.loadUserByUsername("nuthchanreaksa@gmail.com"));

    }

}
