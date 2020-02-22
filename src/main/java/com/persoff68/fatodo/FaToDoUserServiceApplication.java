package com.persoff68.fatodo;

import com.persoff68.fatodo.config.AppProfileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class FaToDoUserServiceApplication {

    public static void main(String[] args) {
        Properties defaultProfileProperties = AppProfileUtil.getDefaultProfile();
        SpringApplication app = new SpringApplication(FaToDoUserServiceApplication.class);
        app.setDefaultProperties(defaultProfileProperties);
        app.run(args);
    }

}
