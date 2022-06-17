package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import static logic.GeneralMethods.round;

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
     * Name of the platform
     */
    private String name;

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
        assert name != null;
        assert !name.isEmpty();
        assert fee >= 0;

        this.name = name;
        this.fee = fee;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        assert name != null;
        assert !name.isEmpty();

        this.name = name;
    }

    @Override
    public double getFee(double price) {
        assert price >= 0;

        return fee;
    }

    @Override
    public String getFxmlPath() {
        return "platformController/AbsolutePlatformController.fxml";
    }

    /**
     * Sets the specified fee of this platform
     *
     * @param fee which should be set
     */
    public void setFee(double fee) {
        assert fee >= 0;

        this.fee = fee;
    }

    @Override
    public double calcSellingExchangeRate(Investment investment, double targetPerformance) {
        assert investment != null;
//        assert targetPerformance >= 0;

        double capital = investment.getCapital();
        double count = capital / investment.getExchangeRate();
        double difference = capital + targetPerformance + 2 * fee;

        return round(difference / count);
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
        return that.fee == fee && name.equals(that.name);
    }
}
