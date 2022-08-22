package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static logic.BigDecimalUtils.isPositive;

/**
 * This class contains the absolute platform logic and
 * is an offshoot of the class platform
 *
 * @author xthe_white_lionx
 * @see Platform
 * @see MixedPlatform
 * @see PercentPlatform
 */
public class AbsolutePlatform extends Platform {

    /**
     * Fee of the platform as absolute value
     */
    private double fee;

    /**
     * Constructs an {@code AbsolutePlatform} with the specified arguments.
     *
     * @param name of the platform
     * @param fee  of the platform as absolute value
     */
    public AbsolutePlatform(String name, double fee) {
        super(name);
        if (fee < 0) {
            throw new IllegalArgumentException();
        }
        this.fee = fee;
    }

    @Override
    public double getFee(BigDecimal price) {
        if (!isPositive(price)) {
            throw new IllegalArgumentException();
        }

        return fee;
    }

    @Override
    public String getFxmlPath() {
        return "platformController/AbsolutePlatform.fxml";
    }

    @Override
    public FeeType getType() {
        return FeeType.ABSOLUTE;
    }

    /**
     * Sets the specified fee of this platform
     *
     * @param fee which should be set
     */
    public void setFee(double fee) {
        if (fee < 0) {
            throw new IllegalArgumentException();
        }

        this.fee = fee;
    }

    @Override
    public BigDecimal calcSellingExchangeRate(Investment investment, BigDecimal targetPerformance) {
        if (investment == null) {
            throw new NullPointerException();
        }

        BigDecimal capital = investment.getCapital();
        BigDecimal count = capital.divide(investment.getExchangeRate());
        BigDecimal difference =
                capital.add(targetPerformance).add(new BigDecimal(2 * fee));

        return difference.divide(count, 2, RoundingMode.HALF_UP);
    }

    @Override
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("fee", new JsonPrimitive(fee));
        return super.toJson(jsonObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsolutePlatform that)) return false;
        if (!super.equals(o)) return false;
        return this.fee == that.fee;
    }
}
