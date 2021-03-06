package com.trading.protrading.strategytesting;

import org.junit.Test;

import static org.junit.Assert.*;

public class RawReportTest {

    public static final double DELTA = 0.01;
    public static final double REPORT_STARTING_FUNDS = 500.7;
    public static final double OPEN_POSITION_FUNDS = 75.2;

    @Test
    public void testOpenPositionChangeCurrentFunds() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);
        assertEquals(REPORT_STARTING_FUNDS, report.getCurrentFunds(), DELTA);

        report.openPosition(OPEN_POSITION_FUNDS);
        assertEquals(425.5, report.getCurrentFunds(), DELTA);
    }

    @Test
    public void testOpenPositionLocksFunds() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);
        assertEquals(0, report.getLockedFunds(), DELTA);

        report.openPosition(OPEN_POSITION_FUNDS);
        assertEquals(OPEN_POSITION_FUNDS, report.getLockedFunds(), DELTA);
    }

    @Test
    public void testClosePositionUpdateCurrentFunds() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(124.37);
        assertEquals(549.87, report.getCurrentFunds(), DELTA);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(70.2);
        assertEquals(544.87, report.getCurrentFunds(), DELTA);

    }

    @Test
    public void testClosePositionUpdateMaxFunds() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(124.37);
        assertEquals(549.87, report.getMaxFunds(), DELTA);

    }

    @Test
    public void testClosePositionDontUpdateMaxFunds() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(70.2);
        assertEquals(REPORT_STARTING_FUNDS, report.getMaxFunds(), DELTA);
    }

    @Test
    public void testClosePositionUpdateGrossProfit() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        assertEquals(0, report.getGrossLosses(), DELTA);
        assertEquals(0, report.getGrossProfit(), DELTA);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(100.2);

        assertEquals(0, report.getGrossLosses(), DELTA);
        assertEquals(25, report.getGrossProfit(), DELTA);
    }

    @Test
    public void testClosePositionUpdateWinsCount() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        assertEquals(0, report.getWinCount());
        assertEquals(0, report.getLossesCount());

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(100.2);

        assertEquals(1, report.getWinCount());
        assertEquals(0, report.getLossesCount());

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(75.2);

        assertEquals(2, report.getWinCount());
        assertEquals(0, report.getLossesCount());
    }

    @Test
    public void testClosePositionUpdateGrossLosses() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        assertEquals(0, report.getGrossLosses(), DELTA);
        assertEquals(0, report.getGrossProfit(), DELTA);

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(50.2);

        assertEquals(25, report.getGrossLosses(), DELTA);
        assertEquals(0, report.getGrossProfit(), DELTA);
    }

    @Test
    public void testClosePositionUpdateLossesCount() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        assertEquals(0, report.getWinCount());
        assertEquals(0, report.getLossesCount());

        report.openPosition(OPEN_POSITION_FUNDS);

        report.closePosition(55.2);

        assertEquals(0, report.getWinCount());
        assertEquals(1, report.getLossesCount());
    }

    @Test
    public void testClosePositionUpdateMaxConsecutiveLosses() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);

        assertEquals(0, report.getMaxConsecutiveLossesCount());
        for (int i = 0; i < 5; i++) {
            report.openPosition(OPEN_POSITION_FUNDS);
            report.closePosition(55.2);
        }

        assertEquals(5, report.getMaxConsecutiveLossesCount());

        report.openPosition(OPEN_POSITION_FUNDS);
        report.closePosition(155.2);

        for (int i = 0; i < 3; i++) {
            report.openPosition(OPEN_POSITION_FUNDS);
            report.closePosition(55.2);
        }

        assertEquals(5, report.getMaxConsecutiveLossesCount());
    }

    @Test
    public void testClosePositionUpdateDrawdownReturn() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);
        assertEquals(0, report.getDrawdownReturn(),DELTA);

        report.openPosition(150.2);
        report.closePosition(75);

        assertEquals(2833.08, report.getDrawdownReturn(),DELTA);
    }

    @Test
    public void testClosePositionUpdateMaxDrawdown() {
        RawReport report = new RawReport(REPORT_STARTING_FUNDS);
        assertEquals(0, report.getMaxDrawdown(), DELTA);

        report.openPosition(75);
        report.closePosition(50.2);

        assertEquals(0.05, report.getMaxDrawdown(),DELTA);

        report.openPosition(300);
        report.closePosition(50.2);

        assertEquals(0.55, report.getMaxDrawdown(),DELTA);

        report.openPosition(50.2);
        report.closePosition(450);

        report.openPosition(150.2);
        report.closePosition(75);

        assertEquals(0.55, report.getMaxDrawdown(),DELTA);
    }
}