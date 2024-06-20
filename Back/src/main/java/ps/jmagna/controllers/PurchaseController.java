package ps.jmagna.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.common.ListDto;
import ps.jmagna.dtos.purchase.DeliveryDto;
import ps.jmagna.dtos.purchase.SellDto;
import ps.jmagna.dtos.purchase.NotificationResponce;
import ps.jmagna.dtos.purchase.PurchaseResponce;
import ps.jmagna.dtos.purchase.SaleDto;
import ps.jmagna.dtos.purchase.PurchaseRequest;
import ps.jmagna.dtos.purchase.PutDeliveryRequest;
import ps.jmagna.enums.DeliveryState;
import ps.jmagna.services.AuthService;
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
    @Autowired
    AuthService authService;


    @PostMapping("/reg")
    public PurchaseResponce reg(@RequestBody PurchaseRequest request,
                                @AuthenticationPrincipal Jwt authentication) throws MPException, MPApiException {
        return service.registerSale(request,authService.findUser(authentication));
    }


    @PostMapping("/not")
    public NotificationResponce notificar(@RequestBody LinkedHashMap data,
                                          @RequestParam(name = "user",defaultValue = "0") String user)
            throws MPException, MPApiException {
        return service.notificar(data,user);
    }

    @GetMapping("/purchases")
    public ListDto<SaleDto> getPurchases(@RequestParam String firstDate,
                                         @RequestParam String lastDate,
                                         @RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @AuthenticationPrincipal Jwt authentication) {
        return service.getPurchases(firstDate,lastDate, name,page,size,authService.findUser(authentication));
    }
    @GetMapping("/sells")
    public ListDto<SellDto> getSells(@RequestParam String firstDate,
                                  @RequestParam String lastDate,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @AuthenticationPrincipal Jwt authentication) {
        return service.getSells(firstDate,lastDate, name ,page,size,authService.findUser(authentication));
    }
    @GetMapping("/deliveries")
    public ListDto<DeliveryDto> getDeliveries(@RequestParam(defaultValue = "PENDIENTE") DeliveryState state,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @AuthenticationPrincipal Jwt authentication) {
        return service.getDeliveries(state,page,size,authService.findUser( authentication));
    }

    @PutMapping("/delivery")
    public DeliveryDto putDelivery(@RequestBody PutDeliveryRequest request){
        return service.putDelivery(request);
    }

    @DeleteMapping("/sell/{id}")
    public boolean deleteSell(@PathVariable Long id,
                              @AuthenticationPrincipal Jwt authentication){
        return service.deleteSell(id,authService.findUser(authentication));
    }
}
