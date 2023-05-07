package com.tenpo.profit.infraestructure.adapters.input.rest;

import com.tenpo.profit.application.ports.input.CalculateProfitUseCase;
import com.tenpo.profit.application.ports.input.GetProfitsUseCase;
import com.tenpo.profit.domain.model.Profit;
import com.tenpo.profit.infraestructure.adapters.input.rest.data.request.ProfitCalculateRequest;
import com.tenpo.profit.infraestructure.adapters.input.rest.data.response.ProfitQueryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/v1/profit")
public class ProfitRestAdapter {
    private final CalculateProfitUseCase calculateProfitUseCase;
    private final GetProfitsUseCase getProfitsUseCase;

    public ProfitRestAdapter(CalculateProfitUseCase calculateProfitUseCase, GetProfitsUseCase getProfitsUseCase) {
        this.calculateProfitUseCase = calculateProfitUseCase;
        this.getProfitsUseCase = getProfitsUseCase;
    }

    @GetMapping
    Iterable<ProfitQueryResponse> getProfits() {

        var profits = getProfitsUseCase.getProfits();

        return toProfitQueryResponse(profits);
    }

    @PostMapping
        // TODO: revisar el return de esta funcion, quizas deberia tener una clase response aparte de query
    ResponseEntity<ProfitQueryResponse> calculateProfit(@RequestBody ProfitCalculateRequest profitRequest) {

        var profit = calculateProfitUseCase.calculateProfit(profitRequest.getOperatorX(), profitRequest.getOperatorY());

        return ResponseEntity.status(HttpStatus.CREATED).body(toProfitCalculateResponse(profit));
    }

    private Iterable<ProfitQueryResponse> toProfitQueryResponse(Iterable<Profit> profit) {

        var response = new ArrayList<ProfitQueryResponse>();

        for (Profit p: profit) {
            response.add(new ProfitQueryResponse(p.getId(), p.getOperatorX(), p.getOperatorY(), p.getTotal()));
        }

        return response;
    }

    private ProfitQueryResponse toProfitCalculateResponse(Profit profit) {
        return new ProfitQueryResponse(profit.getId(), profit.getOperatorX(), profit.getOperatorY(), profit.getTotal());
    }

}
