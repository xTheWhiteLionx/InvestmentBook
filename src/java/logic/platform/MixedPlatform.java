package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.BigDecimalUtils;
import logic.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static logic.DoubleUtils.round;
import static logic.BigDecimalUtils.HUNDRED;
import static logic.BigDecimalUtils.isPositive;

/**
 * This class is contains the mixed platform logic and
 * is an offshoot of the class platform
 *
 * @author xthe_white_lionx
 * @see Platform
 * @see AbsolutePlatform
 * @see PercentPlatform
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
     * @param name    of the platform
     * @param percent value of the fee of the platform
     * @param min     minimale fee of the platform as absolute value
     */
    public MixedPlatform(String name, double percent, double min) {
        super(name);
        this.percent = percent;
        this.minFee = min;
    }

    @Override
    public double getFee(BigDecimal price) {
        if (!isPositive(price)) {

        }
        return Math.max(round((percent / 100d) * price.doubleValue()), minFee);
    }

    @Override
    public String getFxmlPath() {
        return "platformController/MixedPlatform.fxml";
    }

    @Override
    public FeeType getType() {
        return FeeType.MIXED;
    }

    /**
     * Returns the value of the fee of this platform
     *
     * @return value of the fee of this platform
     */
    public double getPercent() {
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
    public BigDecimal calcSellingExchangeRate(Investment investment, BigDecimal targetPerformance) {
        BigDecimal capital = investment.getCapital();
        BigDecimal sellingPriceWithoutSellingFee =
                BigDecimalUtils.add(capital, getFee(capital)).add(targetPerformance);

        //TODO reason why the testSellingPriceCalculatorReal fails!
        BigDecimal antiPercent = BigDecimal.ONE.subtract(BigDecimal.valueOf((percent / 100d)));
        BigDecimal sellingPrice =
        sellingPriceWithoutSellingFee.divide(antiPercent, 2, RoundingMode.HALF_UP);

        if (getFee(sellingPrice) < minFee) {
            sellingPrice = BigDecimalUtils.calcPercent(sellingPriceWithoutSellingFee, BigDecimal.valueOf(minFee));
        }
        BigDecimal percentPerformance = BigDecimalUtils.calcPercent(capital, sellingPrice);

        if (percentPerformance.compareTo(HUNDRED) < 0) {
            percentPerformance = percentPerformance.add(HUNDRED);
        }
        return investment.getExchangeRate().multiply(percentPerformance.divide(HUNDRED));
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("percent", new JsonPrimitive(percent));
        jsonObject.add("min", new JsonPrimitive(minFee));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MixedPlatform that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Double.compare(that.percent, percent) == 0 && Double.compare(that.minFee, minFee) == 0;
    }
}
