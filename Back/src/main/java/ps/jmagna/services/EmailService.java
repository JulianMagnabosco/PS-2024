package ps.jmagna.services;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ps.jmagna.dtos.purchase.SaleDetailDto;
import ps.jmagna.dtos.purchase.SaleDto;
import ps.jmagna.entities.SaleDetailEntity;
import ps.jmagna.entities.SaleEntity;
import ps.jmagna.entities.SectionEntity;
import ps.jmagna.enums.SecType;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class EmailService {

    @Autowired
    JavaMailSender sender;
    @Value("${spring.mail.username}")
    String from;

    public boolean sendEmail(String subject, String textMessage, String receptorEmail) {
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
            System.out.println("emailError: -"+e);
        }
        return send;
    }

    public boolean sendEmailSale(SaleEntity sale, String receptorEmail) {
        BigDecimal total = BigDecimal.ZERO;

        String resume = "";
        for (SaleDetailEntity detail : sale.getDetails()) {
            resume +=
                "<tr>"+
                "   <th style=\"border: 1px solid #dddddd;\"scope=\"row\">"+ detail.getPublication().getId() +"</th>\n" +
                "   <td style=\"border: 1px solid #dddddd;\">"+ detail.getPublication().getName() +"</td>\n" +
                "   <td style=\"border: 1px solid #dddddd;\">$"+ detail.getTotal() +"</td>\n" +
                "   <td style=\"border: 1px solid #dddddd;\">"+ detail.getCount() +"</td>" +
                "</tr>\n";
//            resume +=
//                detail.getPublication().getId() +": " +
//                detail.getPublication().getName() +
//                "($"+ detail.getTotal() +")" +
//                "x" + detail.getCount() +"\n";

            total = total.add(detail.getTotal());
        }

        String table =
                "<table style=\"\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\">\n" +
                "  <thead style=\"background-color: #dddaaa;\">\n" +
                "    <tr>\n" +
                "      <th scope=\"col\">Id</th>\n" +
                "      <th scope=\"col\">Nombre de la obra</th>\n" +
                "      <th scope=\"col\">Total</th>\n" +
                "      <th scope=\"col\">Cantidad</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>\n" +
                "dataTrans"+
                "  </tbody>\n" +
                "</table>";

        String dataString="<div>Fecha de la transacción: "+
                sale.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"))+"</div>"+
                "<div>Estado: "+sale.getSaleState().toString()+"</div>"+
                "<div>Total: $"+total+"</div>"+
                "<div>Resumen:</div>"+table.replace("dataTrans",resume);

        return sendEmail("Compra",
                dataString,
                receptorEmail);
    }

}