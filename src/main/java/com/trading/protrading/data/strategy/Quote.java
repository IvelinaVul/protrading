package com.trading.protrading.data.strategy;


import java.time.LocalDateTime;

public class Quote {
    private Asset asset;
    private double price;
    private QuoteType type;
    private LocalDateTime date;

    public Quote(Asset asset, double price, QuoteType type, LocalDateTime date) {
        this.asset = asset;
        this.price = price;
        this.type = type;
        this.date = date;
    }

    public Asset getAsset() {
        return asset;
    }

    public double getPrice() {
        return price;
    }

    public QuoteType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }
}