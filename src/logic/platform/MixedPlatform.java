package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import static helper.GeneralMethods.calcPercent;
import static helper.GeneralMethods.round;

/**
 * This class is contains the mixed platform logic and
 * is an offshoot of the class platform
 *
 * @see Platform
 * @see AbsolutePlatform
 * @see PercentPlatform
 * @author xthe_white_lionx
 */
public class MixedPlatform extends Platform {

    /**
     * Value of the fee of the platform
     */
    private double percent;

    /**
     * Minimale Fee of the platform as absolute value
     */
    private double minFee;

    /**
     * Constructs an {@code MixedPlatform} with the specified arguments.
     *
     * @param name of the platform
     * @param percent value of the fee of the platform
     * @param min minimale fee of the platform as absolute value
     */
    public MixedPlatform(String name, double percent, double min) {
        super(name);
        this.percent = percent;
        this.minFee = min;
    }

    @Override
    public double getFee(double price) {
        return Math.max(round((percent / 100d) * price), minFee);
    }

    @Override
    public String getFxmlPath() {
        return "platformController/MixedPlatform.fxml";
    }

    @Override
    public FeeType getTyp() {
        return FeeType.MIXED;
    }

    /**
     * Returns the value of the fee of this platform
     *
     * @return value of the fee of this platform
     */
    public double getPercent(){
        return percent;
    }

    /**
     * Sets the value of the fee of this platform to the specified percent
     *
     * @param percent which should be set
     */
    public void setPercent(double percent) {
        this.percent = percent;
    }

    /**
     * Returns the minimale Fee of the platform as absolute value
     *
     * @return minimale Fee of the platform as absolute value
     */
    public double getMinFee() {
        return minFee;
    }

    /**
     * Sets the minimale Fee of the platform to the specified percent
     *
     * @param minFee which should be set
     */
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
        return round(investment.getExchangeRate() * (percentPerformance / 100d));
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("percent", new JsonPrimitive(percent));
        jsonObject.add("min", new JsonPrimitive(minFee));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MixedPlatform that)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(that.percent, percent) == 0 && Double.compare(that.minFee, minFee) == 0;
    }
}
