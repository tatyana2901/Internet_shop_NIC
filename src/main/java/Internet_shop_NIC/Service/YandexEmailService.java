package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.OrderConfirmationToEmailResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class YandexEmailService {
    private final JavaMailSender mailSender;
    private final String senderEmail;

    @Autowired
    public YandexEmailService(JavaMailSender mailSender, @Value("${shop.email.sender}") String senderEmail) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendOrderConfirmation(String to, Double ordersTotalPrice, Long ordersNumber, Iterable<OrderConfirmationToEmailResponse> items) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(senderEmail);
        msg.setTo(to);
        msg.setSubject("Вы оформили заказ № " + ordersNumber + ":");
        msg.setText(items.toString() + " \\nОбщая сумма заказа составила " + ordersTotalPrice + " рублей.");
        mailSender.send(msg);
    }
}
