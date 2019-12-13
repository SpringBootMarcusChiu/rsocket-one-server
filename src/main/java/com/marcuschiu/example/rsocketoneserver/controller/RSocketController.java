package com.marcuschiu.example.rsocketoneserver.controller;

import com.marcuschiu.example.rsocketoneserver.model.MarketDataModel;
import com.marcuschiu.example.rsocketoneserver.model.MarketDataRequest;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Controller
public class RSocketController {

    private ArrayList<MarketDataModel> marketDataModels;

    public RSocketController() {
        marketDataModels = new ArrayList<>();
        marketDataModels.add(new MarketDataModel(0, "zero", null));
    }

    @MessageMapping("getMarketData")
    public Mono<MarketDataModel> getMarketData(MarketDataRequest request) {
        MarketDataModel marketDataModel = null;
        for (MarketDataModel m : marketDataModels) {
            if (m.getId().equals(request.getMarketDataID())) {
                marketDataModel = m;
            }
        }
        return Mono.justOrEmpty(marketDataModel);
    }

    @MessageMapping("allMarketData")
    public Flux<MarketDataModel> allMarketData() {
        return Flux.fromIterable(marketDataModels);
    }

    @MessageMapping("uploadMarketData")
    public Mono<Void> uploadMarketData(MarketDataModel marketDataModel) {
        marketDataModels.add(marketDataModel);
        return Mono.empty();
    }

    @MessageExceptionHandler
    public Mono<MarketDataModel> handleException(Exception e) {
        return Mono.just(MarketDataModel.fromException(e));
    }
}