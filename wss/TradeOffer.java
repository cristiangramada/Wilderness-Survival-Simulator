package wss;

/**
 * One exchange proposal: what the explorer puts on the table and what they expect back from the trader.
 * Stall convention: the merchant only ever asks for <b>gold</b> from you and pays out either <b>water</b>
 * or <b>food</b> (never both in one deal). Traders have unlimited stock; the explorer must have the gold.
 */
public final class TradeOffer {

    private final int giveGold;
    private final int giveWater;
    private final int giveFood;
    private final int receiveGold;
    private final int receiveWater;
    private final int receiveFood;
    private final String note;

    public TradeOffer(int giveGold, int giveWater, int giveFood,
            int receiveGold, int receiveWater, int receiveFood,
            String note) {
        this.giveGold = giveGold;
        this.giveWater = giveWater;
        this.giveFood = giveFood;
        this.receiveGold = receiveGold;
        this.receiveWater = receiveWater;
        this.receiveFood = receiveFood;
        this.note = note == null ? "" : note;
    }

    public int getGiveGold() {
        return giveGold;
    }

    public int getGiveWater() {
        return giveWater;
    }

    public int getGiveFood() {
        return giveFood;
    }

    public int getReceiveGold() {
        return receiveGold;
    }

    public int getReceiveWater() {
        return receiveWater;
    }

    public int getReceiveFood() {
        return receiveFood;
    }

    public String getNote() {
        return note;
    }

    public TradeOffer copy() {
        return new TradeOffer(giveGold, giveWater, giveFood, receiveGold, receiveWater, receiveFood, note);
    }

    /** True if this proposal matches the merchant's standing sheet exactly. */
    public boolean matchesStanding(TradeOffer other) {
        if (other == null) {
            return false;
        }
        return giveGold == other.giveGold && giveWater == other.giveWater && giveFood == other.giveFood
                && receiveGold == other.receiveGold && receiveWater == other.receiveWater
                && receiveFood == other.receiveFood;
    }

    /**
     * True when this follows the stall rules: gold-only payment, no gold from the merchant,
     * and exactly one of water or food on the receive side is positive.
     */
    public boolean conformsToStallRules() {
        if (giveWater != 0 || giveFood != 0 || receiveGold != 0) {
            return false;
        }
        boolean water = receiveWater > 0;
        boolean food = receiveFood > 0;
        return water != food;
    }

    public boolean playerCanPay(Player p) {
        return p.getGold() >= giveGold && p.getWater() >= giveWater && p.getFood() >= giveFood;
    }

    public void apply(Player p) {
        p.addGold(-giveGold);
        p.addWater(-giveWater);
        p.addFood(-giveFood);
        p.addGold(receiveGold);
        p.addWater(receiveWater);
        p.addFood(receiveFood);
    }

    /** Rough ledger score for comparing generosity of two offers. */
    public int ledgerWeight() {
        return giveGold * 10 + giveWater * 2 + giveFood * 3
                - (receiveGold * 10 + receiveWater * 2 + receiveFood * 3);
    }

    /**
     * Merchant softens: lower gold ask and slightly less water <i>or</i> food returned (whichever they sell),
     * never below zero.
     */
    public TradeOffer softenAsk(int dropGoldAsk, int dropStockGiven) {
        int ng = Math.max(0, giveGold - dropGoldAsk);
        int rw = receiveWater;
        int rf = receiveFood;
        if (receiveWater > 0) {
            rw = Math.max(0, receiveWater - dropStockGiven);
        } else if (receiveFood > 0) {
            rf = Math.max(0, receiveFood - dropStockGiven);
        }
        return new TradeOffer(ng, giveWater, giveFood, receiveGold, rw, rf, "");
    }

    /** Wording {@code "12 gold for 5 food"}. */
    public String plainStallLine() {
        if (receiveWater > 0) {
            return giveGold + " gold for " + receiveWater + " water";
        }
        if (receiveFood > 0) {
            return giveGold + " gold for " + receiveFood + " food";
        }
        return giveGold + " gold";
    }

    public String summarizeForLog() {
        return plainStallLine();
    }
}
