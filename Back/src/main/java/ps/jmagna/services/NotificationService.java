package ps.jmagna.services;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ps.jmagna.dtos.notification.NotificationDto;
import ps.jmagna.entities.NotificationEntity;
import ps.jmagna.entities.SaleDetailEntity;
import ps.jmagna.entities.SaleEntity;
import ps.jmagna.entities.UserEntity;
import ps.jmagna.repository.NotificationRepository;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository repository;
    @Autowired
    JavaMailSender sender;
    @Value("${spring.mail.username}")
    String from;
    @Autowired
    ModelMapper modelMapper;


    public List<NotificationDto> getAll(int size, UserEntity user){
        Pageable pageable = PageRequest.of(0,size, Sort.by("dateTime").descending());
        Page<NotificationEntity> entities= repository.findAllByDeletedIsFalseAndUser(user,pageable);

        List<NotificationDto> list = new ArrayList<>();
        for (NotificationEntity e : entities){
            list.add(modelMapper.map(e,NotificationDto.class));
        }
        return list;
    }

    public boolean sendNotificationSale(SaleEntity sale){
        if(repository.existsByCode("purchase_"+sale.getId())) return true;
        for (SaleDetailEntity s: sale.getDetails()) {
            sendNotification( "sell_"+s.getId(), "Venta Completada",

                    "("+s.getCount()+")"+
                            s.getPublication().getName(),

                    "<div>Obra vendida: "+ s.getPublication().getName()+"</div>"+
                            "<div>Cantidad: "+ s.getCount()+"</div>"+
                            "<div>Total: "+ s.getTotal()+"</div>"+
                            "<div>Comprador: "+ sale.getUser().getUsername()+"</div>",

                    s.getPublication().getUser());
        }
        registerNotification( "purchase_"+sale.getId(), "Compra Completada",
                "("+sale.getDetails().get(0).getCount()+")"+
                        sale.getDetails().get(0).getPublication().getName()+"...",  sale.getUser());
        sendEmailSale( sale,  sale.getUser().getEmail());

        return true;
    }
    private boolean sendEmailSale(SaleEntity sale, String receptorEmail) {
        BigDecimal total = BigDecimal.ZERO;

        String items = "";
        for (SaleDetailEntity detail : sale.getDetails()) {
            items +=
                    "<tr>"+
                            "   <th style=\"border: 1px solid #dddddd;\"scope=\"row\">"+
                            detail.getPublication().getId() +"</th>\n" +
                            "   <td style=\"border: 1px solid #dddddd;\">"+
                            detail.getPublication().getName() +"</td>\n" +
                            "   <td style=\"border: 1px solid #dddddd;\">$"+
                            detail.getTotal() +"</td>\n" +
                            "   <td style=\"border: 1px solid #dddddd;\">"+
                            detail.getCount() +"</td>\n" +
                            "</tr>\n";

            total = total.add(detail.getTotal());
        }

        String table =
                """
                    <table style="
                        border-collapse: collapse;
                        width: 100%;">
                        <thead style="background-color: #dddaaa;">
                            <tr>
                              <th scope="col">Id</th>
                              <th scope="col">Nombre de la obra</th>
                              <th scope="col">Total</th>
                              <th scope="col">Cantidad</th>
                            </tr>
                        </thead>
                        <tbody>
                            dataTrans
                        </tbody>
                    </table>
                """;

        String dataString="<div>Fecha de la transacción: "+
                sale.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"))+"</div>"+
                "<div>Estado: "+sale.getSaleState().toString()+"</div>"+
                "<div>Total: $"+total+"</div>"+
                "<div>Resumen:</div>"+table.replace("dataTrans",items);

        return sendEmail("Compra",
                dataString,
                receptorEmail);
    }
    public boolean sendNotification(String code, String subject,
                                    String textMessage, String emailMessage,
                                    UserEntity user){
        if(repository.existsByCode(code)) {
            return false;
        }
        if(!registerNotification( code, subject,  textMessage,  user)) {
            return false;
        }

        return sendEmail(subject, emailMessage, user.getEmail());
    }
    private boolean registerNotification(String code,String subject, String textMessage, UserEntity user){
        NotificationEntity notification = new NotificationEntity();
        notification.setDeleted(false);
        notification.setDateTime(LocalDateTime.now());
        notification.setCode(code);
        notification.setTitle(subject);
        notification.setText(textMessage);
        notification.setUser(user);
        try {
            repository.save(notification);
        }catch (Exception e){
            System.out.println("notificationError: -"+e.getMessage());
            return false;
        }
        return true;
    }
    private boolean sendEmail(String subject, String textMessage, String receptorEmail) {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(new InternetAddress(from, "COMOloHAGO"));
            helper.setTo(receptorEmail);
            String htmlTemplate = String.valueOf(readFile("src/main/resources/templateMail.html"));
            String text = htmlTemplate.replace("datahtml",textMessage);
//            System.out.println("html: "+text);
            helper.setText(text, true);
            helper.setSubject(subject);
            sender.send(message);
            send = true;
        } catch (Exception e) {
            System.out.println("emailError: -"+e.getMessage());
        }
        return send;
    }


}