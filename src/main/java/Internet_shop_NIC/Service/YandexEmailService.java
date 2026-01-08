package Internet_shop_NIC.Service;

import Internet_shop_NIC.Entity.OrderItem;
import Internet_shop_NIC.Entity.Orders;
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

    public void sendOrderConfirmation(String to, Orders order, Iterable<OrderItem> items) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ваш@yandex.ru");
        msg.setTo(to);
        msg.setSubject("Заказ оформлен");
        msg.setText("Текст...");
        mailSender.send(msg);
    }
}
