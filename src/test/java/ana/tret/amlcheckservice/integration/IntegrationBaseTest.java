package ana.tret.amlcheckservice.integration;

import ana.tret.amlcheckservice.TestcontainersConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestcontainersConfiguration.class)
public abstract class IntegrationBaseTest {}
