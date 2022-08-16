package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import static helper.GeneralMethods.round;

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
    public double getFee(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }

        return fee;
    }

    @Override
    public String getFxmlPath() {
        return "platformController/AbsolutePlatform.fxml";
    }

    @Override
    public FeeType getTyp() {
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
    public double calcSellingExchangeRate(Investment investment, double targetPerformance) {
        if (investment == null) {
            throw new NullPointerException();
        }

        double capital = investment.getCapital();
        double count = capital / investment.getExchangeRate();
        double difference = capital + targetPerformance + 2 * fee;

        return round(difference / count);
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
        return Double.compare(that.fee, fee) == 0;
    }
}