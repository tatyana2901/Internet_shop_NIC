package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class YandexEmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private YandexEmailService emailService;

    private final String TEST_SENDER_EMAIL = "shop@example.com";
    private final String TEST_RECIPIENT_EMAIL = "customer@example.com";

    @BeforeEach
    void setUp() {
        emailService = new YandexEmailService(mailSender, TEST_SENDER_EMAIL);
    }

    @Test
    void sendOrderConfirmation_ShouldSendEmail() {
        Double totalPrice = 1500.0;
        Long orderNumber = 123L;

        OrderConfirmationToEmailResponse item1 =
                new OrderConfirmationToEmailResponse("Товар 1", 500.0, 2);
        OrderConfirmationToEmailResponse item2 =
                new OrderConfirmationToEmailResponse("Товар 2", 250.0, 2);

        List<OrderConfirmationToEmailResponse> items = Arrays.asList(item1, item2);

        emailService.sendOrderConfirmation(TEST_RECIPIENT_EMAIL, totalPrice, orderNumber, items);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }


}