package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.util.Objects;

import static logic.GeneralMethods.*;

//TODO JavaDoc
public class PercentPlatform extends Platform {

    //TODO JavaDoc
    private final String name;

    //TODO JavaDoc
    private double percent;

    //TODO JavaDoc
    public PercentPlatform(String name, double percent) {
        this.name = name;
        this.percent = percent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getFee(double price) {
        return roundDouble((percent /100d) * price);
    }

    //TODO JavaDoc
    public double getPercent(){
        return percent;
    }

    //TODO JavaDoc
    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public double calcSellingExchangeRate(Investment investment, double targetPerformance) {
        double capital = investment.getCapital();
        double sellingPrice = (capital + targetPerformance + getFee(capital)) / (1 - percent / 100d);

        System.out.println("expected: " + investment.getSellingPrice());
        System.out.printf("calcPercent: %.2f%n", sellingPrice);

        //TODO problem could be here for testSellingPriceCalculator
        double percentPerformance = calcPercent(capital, sellingPrice);

        if (100 > percentPerformance) {
            percentPerformance += 100;
        }

        return roundDouble(investment.getExchangeRate() * (percentPerformance / 100d));
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("Percent", new JsonPrimitive(percent));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PercentPlatform that)) return false;
        return that.percent == percent && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, percent);
    }
}
