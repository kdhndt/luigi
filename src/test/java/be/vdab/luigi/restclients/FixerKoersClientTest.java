package be.vdab.luigi.restclients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

//Spring is now connected to JUnit and creates container for bean
@ExtendWith(SpringExtension.class)
//Spring can read file
@PropertySource("application.properties")
//Spring creates a bean for testing
@Import(FixerKoersClient.class)

class FixerKoersClientTest {
    private final FixerKoersClient client;

    //constructor injecteert nu automatisch de Spring bean FixerKoersClient, mogelijk gemaakt door de annotations en het spring.properties bestand in de resources directory
    FixerKoersClientTest(FixerKoersClient client) {
        this.client = client;
    }

    @Test
    void deKoersIsPositief() {
        assertThat(client.getDollarKoers()).isPositive();
    }

}