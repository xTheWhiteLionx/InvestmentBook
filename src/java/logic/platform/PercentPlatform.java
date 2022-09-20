package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.BigDecimalUtils;
import logic.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static logic.BigDecimalUtils.HUNDRED;

/**
 * This class is contains the percent platform logic and
 * is an offshoot of the class platform
 *
 * @author xthe_white_lionx
 * @see Platform
 * @see AbsolutePlatform
 * @see MixedPlatform
 */
public class PercentPlatform extends Platform {

    /**
     * Value of the fee of the platform
     */
    private double percent;

    /**
     * Constructs an {@code PercentPlatform} with the specified arguments.
     *
     * @param id
     * @param name    name of the platform
     * @param percent value of the fee of the platform in percent
     */
    public PercentPlatform(String name, double percent) {
        super(name);
        this.percent = percent;
    }

    @Override
    public double getFee(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(percent / 100d)).doubleValue();
    }

    @Override
    public String getFxmlPath() {
        return "platformController/PercentPlatform.fxml";
    }

    @Override
    public FeeType getType() {
        return FeeType.PERCENT;
    }

    /**
     * Returns the value of the fee of the platform
     *
     * @return value of the fee of the platform
     */
    public double getPercent() {
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
    public BigDecimal calcSellingExchangeRate(Investment investment, BigDecimal targetPerformance) {
        BigDecimal capital = investment.getCapital();
        BigDecimal antiPercent = BigDecimal.valueOf(1 - percent / 100d);
        BigDecimal sellingPrice = BigDecimalUtils.add(capital.add(targetPerformance),
                getFee(capital)).divide(antiPercent, 2, RoundingMode.HALF_UP);

        System.out.println("expected: " + investment.getSellingPrice());
        System.out.printf("calcPercent: %.2f%n", sellingPrice);

        //TODO problem could be here for testSellingPriceCalculator
        BigDecimal percentPerformance = BigDecimalUtils.calcPercent(capital, sellingPrice);

        if (percentPerformance.compareTo(HUNDRED) < 0) {
            percentPerformance = percentPerformance.add(HUNDRED);
        }

        return investment.getExchangeRate().multiply(percentPerformance.divide(HUNDRED, 2,
                RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("percent", new JsonPrimitive(percent));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PercentPlatform that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Double.compare(that.percent, percent) == 0;
    }
}
