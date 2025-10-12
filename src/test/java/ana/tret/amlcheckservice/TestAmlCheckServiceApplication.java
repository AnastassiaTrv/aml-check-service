package ana.tret.amlcheckservice;

import org.springframework.boot.SpringApplication;

public class TestAmlCheckServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AmlCheckServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
