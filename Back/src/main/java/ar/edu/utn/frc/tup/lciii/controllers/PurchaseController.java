package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.SellDto;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.NotPurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.PurchaseResponce;
import ar.edu.utn.frc.tup.lciii.dtos.purchase.SaleDto;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PurchaseRequest;
import ar.edu.utn.frc.tup.lciii.services.impl.PurchaseService;
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
                                      @RequestParam Long user) {
        return service.getPurchases(firstDate,lastDate,user);
    }
    @GetMapping("/sells")
    public List<SellDto> getSells(@RequestParam String firstDate,
                                  @RequestParam String lastDate,
                                  @RequestParam Long user) {
        return service.getSells(firstDate,lastDate,user);
    }

    @PostMapping("/regsingle")
    public PurchaseResponce reg(@RequestBody PurchaseRequest request) throws MPException, MPApiException {
        return service.registerSale(request);
    }


    @PostMapping("/not")
    public NotPurchaseResponce notificar(@RequestBody LinkedHashMap data,@RequestParam(name = "user",defaultValue = "0") String user) {
        return service.notificar(data,user);
    }
}
