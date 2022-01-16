package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.util.Objects;

import static logic.GeneralMethods.calcPercent;
import static logic.GeneralMethods.roundDouble;

//TODO JavaDoc
public class AbsolutePlatform extends Platform {

    //TODO JavaDoc
    private final String NAME;
    //TODO JavaDoc
    private double fee;

    //TODO JavaDoc
    public AbsolutePlatform(String name, double fee) {
        assert !name.isEmpty();
        assert fee >= 0;

        this.NAME = name;
        this.fee = fee;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getFee(double price) {
        assert price >= 0;
        return fee;
    }

    //TODO JavaDoc
    public double getFee() {
        return fee;
    }

    //TODO JavaDoc
    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public double calcSellingExchangeRate(Investment investment, double targetPerformance) {
        assert investment != null;
        assert targetPerformance >= 0;

        double capital = investment.getCapital();
        double difference = capital + targetPerformance + 2 * fee;

        System.out.println("expected: " + investment.getSellingPrice());
        System.out.printf("calcPercent: %.2f%n", difference);

        double percentPerformance = calcPercent(capital, difference);

        if (100 > percentPerformance) {
            percentPerformance += 100;
        }
        return roundDouble(investment.getExchangeRate() * (percentPerformance / 100d));
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("Fee", new JsonPrimitive(fee));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsolutePlatform that)) return false;
        return that.fee == fee && NAME.equals(that.NAME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NAME, fee);
    }
}
