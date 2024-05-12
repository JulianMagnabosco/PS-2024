package ps.jmagna.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.purchase.DeliveryDto;
import ps.jmagna.dtos.purchase.SellDto;
import ps.jmagna.dtos.purchase.NotificationResponce;
import ps.jmagna.dtos.purchase.PurchaseResponce;
import ps.jmagna.dtos.purchase.SaleDto;
import ps.jmagna.dtos.purchase.PurchaseRequest;
import ps.jmagna.dtos.purchase.PutDeliveryRequest;
import ps.jmagna.services.PurchaseService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/sell")
public class PurchaseController {

    @Autowired
    PurchaseService service;

    @GetMapping("/purchases")
    public List<SaleDto> getPurchases(@RequestParam String firstDate,
                                      @RequestParam String lastDate,
                                      @RequestParam String name,
                                      @AuthenticationPrincipal Jwt authentication) {
        return service.getPurchases(firstDate,lastDate, name,authentication.getSubject());
    }
    @GetMapping("/sells")
    public List<SellDto> getSells(@RequestParam String firstDate,
                                  @RequestParam String lastDate,
                                  @RequestParam String name,
                                  @AuthenticationPrincipal Jwt authentication) {
        return service.getSells(firstDate,lastDate, name ,authentication.getSubject());
    }
    @GetMapping("/deliveries")
    public List<DeliveryDto> getDeliveriesPending(@AuthenticationPrincipal Jwt authentication) {

        System.out.println(authentication.toString());
        return service.getDeliveriesPending(authentication.getSubject());
    }

    @PostMapping("/reg")
    public PurchaseResponce reg(@RequestBody PurchaseRequest request,
                                @AuthenticationPrincipal Jwt authentication) throws MPException, MPApiException {
        return service.registerSale(request,authentication.getSubject());
    }


    @PostMapping("/not")
    public NotificationResponce notificar(@RequestBody LinkedHashMap data,
                                          @RequestParam(name = "user",defaultValue = "0") String user)
                                        throws MPException, MPApiException {
        return service.notificar(data,user);
    }

    @PutMapping("/delivery")
    public DeliveryDto putDelivery(@RequestBody PutDeliveryRequest request){
        return service.putDelivery(request);
    }

    @DeleteMapping("/sell/{id}")
    public boolean putSell(@PathVariable Long id){
        return service.deleteSell(id);
    }
}
