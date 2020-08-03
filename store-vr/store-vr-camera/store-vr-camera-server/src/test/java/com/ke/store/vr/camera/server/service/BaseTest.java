package com.ke.store.vr.camera.server.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {"logging.path=logs", "eureka.client.registerWithEureka=false"
    // ,"spring.profiles.active=off"
    })
@DirtiesContext
// @Transactional
// @Rollback
public class BaseTest {

}
