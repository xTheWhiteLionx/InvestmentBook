package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

import java.util.Objects;

/**
 * This class contains the platform logic
 *
 * @author xthe_white_lionx
 * @see AbsolutePlatform
 * @see MixedPlatform
 * @see PercentPlatform
 */
public abstract class Platform {

    /**
     * Name of the platform
     */
    private String name;

    /**
     *
     * @param name
     */
    // TODO: 22.06.2022 JavaDoc
    public Platform(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Constructs an {@code MixedPlatform} with the specified arguments.
     *
     * @param name of the platform
     * @param fee value of the fee of the platform
     * @param minFee minimale fee of the platform as absolute value
     */
    public static Platform create(FeeType feeType, String name, double fee, double minFee) {
        return switch (feeType) {
            case PERCENT -> new PercentPlatform(name, fee);
            case ABSOLUTE -> new AbsolutePlatform(name, fee);
            case MIXED -> new MixedPlatform(name, fee, minFee);
        };
    }

    /**
     * @param jsonObject
     * @return
     */
    //TODO JavaDoc
    public static Platform fromJson(JsonObject jsonObject) {
        String name = jsonObject.get("name").getAsString();
        if (jsonObject.has("fee")) {
            return new AbsolutePlatform(name, jsonObject.get("fee").getAsDouble());
        } else if (jsonObject.has("min")) {
            return new MixedPlatform(name, jsonObject.get("percent").getAsDouble(),
                    jsonObject.get("min").getAsDouble());
        } else {
            return new PercentPlatform(name, jsonObject.get("percent").getAsDouble());
        }
    }

    /**
     * Returns the name of this platform
     *
     * @return name of this platform
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this platform
     *
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public void setName(String name) {
        assert name != null;
        assert !name.isEmpty();

        this.name = name;
    }

    /**
     * Returns the fee of the platform (for a given price)
     *
     * @param price on which the fee will be calculated
     * @return fee of the platform (for a given price)
     * @throws IllegalArgumentException if price is negative
     */
    public abstract double getFee(double price);

    /**
     * Returns the fxmlPath of the platform type
     *
     * @return fxmlPath of the platform type
     */
    public abstract String getFxmlPath();

    /**
     * Calculates the selling price for the given investment to
     * reach the given target performance
     *
     * @param investment        the given investment
     * @param targetPerformance the given target performance, which should be reached
     * @return exchange rate at which the target performance will be reached
     * @throws NullPointerException     if investment is null
     */
    public abstract double calcSellingExchangeRate(Investment investment, double targetPerformance);

    /**
     * Returns the
     *
     * @return
     */
    //TODO JavaDoc
    public JsonObject toJson() {
        return toJson(new JsonObject());
    }

    /**
     * Returns the
     *
     * @param jsonObject
     * @return
     */
    //TODO JavaDoc
    public JsonObject toJson(JsonObject jsonObject) {
        jsonObject.add("name", new JsonPrimitive(getName()));

        return jsonObject;
    }

    /**
     * Returns a string representation of the platform
     *
     * @return string representation of the platform
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Platform platform)) return false;
        return Objects.equals(name, platform.name);
    }
}
