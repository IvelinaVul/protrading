package com.trading.protrading.backtesting;

import com.trading.protrading.data.strategy.Asset;
import com.trading.protrading.data.strategy.Quote;
import com.trading.protrading.model.Strategy;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StrategyTest {
    private TestIdentifier identifier;
    private Strategy strategy;
    private LocalDateTime start;
    private LocalDateTime end;
    private double funds;
    private Asset asset;
    private RawReport rawReport;
    private final UUID reportId;
    private Trade trade;
    private double transactionBuyFunds;
    private double lockedFunds;

    public StrategyTest(TestConfiguration configuration, Strategy strategy, UUID reportId) {
        this.identifier = new TestIdentifier(configuration.getUsername(), configuration.getStrategyName());
        this.strategy = strategy;
        this.start = configuration.getStart();
        this.end = configuration.getEnd();
        this.funds = configuration.getFunds();
        this.asset = configuration.getAsset();
        this.rawReport = new RawReport(funds);
        this.reportId = reportId;
        this.trade = new Trade();
    }

    public StrategyTest(TestIdentifier identifier) {
        this.identifier = identifier;
        strategy = null;
        start = null;
        end = null;
        funds = 0;
        asset = null;
        rawReport = null;
        reportId = null;
        trade = null;
    }

    public TestIdentifier getIdentifier() {
        return identifier;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public double getFunds() {
        return funds;
    }

    public Asset getAsset() {
        return asset;
    }

    public RawReport getRawReport() {
        return rawReport;
    }

    public UUID getReportId() {
        return reportId;
    }

    public void execute(Quote quote) {
        strategy.execute(quote, this);
    }

    public void openTrade(Quote quote, double stopLoss) {
        if (funds < stopLoss) {
            return;
        } else {
            double buyFunds = Math.min(transactionBuyFunds, funds - stopLoss);
            funds -= buyFunds + stopLoss;
            lockedFunds = stopLoss;
            trade.open(quote.getPrice(), buyFunds);
            rawReport.openPosition(buyFunds);
        }
    }

    public void closeTrade(double price) {
        double outcome = trade.close(price);
        funds += outcome + lockedFunds;
        lockedFunds = 0;
        rawReport.closePosition(outcome);
    }

    public double getTradeOpeningPrice() {
        return trade.getOpeningPrice();
    }

    public boolean tradeIsOpen() {
        return trade.isOpen();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrategyTest that = (StrategyTest) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}