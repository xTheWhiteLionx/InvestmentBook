package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.util.Objects;

import static logic.GeneralMethods.*;
//TODO JavaDoc
public class MixedPlatform extends Platform {

    //TODO JavaDoc
    private final String NAME;
    //TODO JavaDoc
    private double percent;
    //TODO JavaDoc
    private double minFee;

    //TODO JavaDoc
    public MixedPlatform(String name, double percent, double min) {
        this.NAME = name;
        this.percent = percent;
        this.minFee = min;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getFee(double price) {
        return Math.max(roundDouble((percent / 100d) * price), minFee);
    }

    //TODO JavaDoc
    public double getPercent(){
        return percent;
    }

    //TODO JavaDoc
    public void setPercent(double percent) {
        this.percent = percent;
    }

    //TODO JavaDoc
    public double getMinFee() {
        return minFee;
    }

    //TODO JavaDoc
    public void setMinFee(double minFee) {
        this.minFee = minFee;
    }

    @Override
    public double calcSellingExchangeRate(Investment investment, double targetPerformance) {
        double capital = investment.getCapital();
        double sellingPriceWithoutSellingFee = capital + targetPerformance + getFee(capital);

        //TODO reason why the testSellingPriceCalculatorReal fails!
        double sellingPrice = sellingPriceWithoutSellingFee / (1 - (percent / 100d));

        if (getFee(sellingPrice) < minFee){
            sellingPrice = sellingPriceWithoutSellingFee + minFee;
        }
        double percentPerformance = calcPercent(capital, sellingPrice);

        if (100 > percentPerformance) {
            percentPerformance += 100;
        }
        return roundDouble(investment.getExchangeRate() * (percentPerformance / 100d));
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("Percent", new JsonPrimitive(percent));
        jsonObject.add("Min", new JsonPrimitive(minFee));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MixedPlatform that)) return false;
        return that.percent == percent && that.minFee == minFee && NAME.equals(that.NAME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NAME, percent, minFee);
    }
}
