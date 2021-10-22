package be.vdab.luigi.services;

import be.vdab.luigi.restclients.KoersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EuroServiceTest {
    @Mock
    private KoersClient koersClient;
    private DefaultEuroService euroService;
    @BeforeEach
    void beforeEach() {
        euroService = new DefaultEuroService(koersClient);
    }
    @Test
    void naarDollar() {
        when(koersClient.getDollarKoers()).thenReturn(BigDecimal.valueOf(1.5));
        assertThat(euroService.naarDollar(BigDecimal.valueOf(2)))
                .isEqualByComparingTo("3");
        //controleren of de method werd opgeroepen
        verify(koersClient).getDollarKoers();
    }
}