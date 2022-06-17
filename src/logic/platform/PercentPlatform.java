package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.util.Objects;

import static logic.GeneralMethods.*;

/**
 * This class is contains the percent platform logic and
 * is an offshoot of the class platform
 *
 * @see Platform
 * @see AbsolutePlatform
 * @see MixedPlatform
 * @author xthe_white_lionx
 */
public class PercentPlatform extends Platform {

    /**
     * Name of the platform
     */
    private String name;

    /**
     * Value of the fee of the platform
     */
    private double percent;

    /**
     * Constructs an {@code PercentPlatform} with the specified arguments.
     *
     * @param name of the platform
     * @param percent value of the fee of the platform
     */
    public PercentPlatform(String name, double percent) {
        this.name = name;
        this.percent = percent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getFee(double price) {
        return round((percent /100d) * price);
    }

    @Override
    public String getFxmlPath() {
        return "platformController/PercentPlatformController.fxml";
    }

    /**
     * Returns the value of the fee of the platform
     *
     * @return value of the fee of the platform
     */
    public double getPercent(){
        return percent;
    }

    /**
     * Sets the value of the fee of the platform to the specified percent
     *
     * @param percent which should be set
     */
    public void setPercent(double percent) {
        assert percent >= 0;

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

        return round(investment.getExchangeRate() * (percentPerformance / 100d));
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
