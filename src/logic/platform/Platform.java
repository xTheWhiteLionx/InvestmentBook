package logic.platform;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import logic.Investment;

//TODO JavaDoc
public abstract class Platform {

    //TODO JavaDoc
    public abstract String getName();

    //TODO JavaDoc
    public abstract double getFee(double price);

    /**
     * Calculates the selling price for the given investment to reach the given target performance
     *
     * @param investment            the given investment
     * @param targetPerformance the given targetPerformance, which should be reached
     * @return exchange rate at which the targetPerformance will be reached
     */
    //TODO JavaDoc
    public abstract double calcSellingExchangeRate(Investment investment, double targetPerformance);

    //TODO JavaDoc
    public JsonObject toJson(){
        return toJson(new JsonObject());
    }

    //TODO JavaDoc
    public JsonObject toJson(JsonObject jsonObject){
        jsonObject.add("Name",new JsonPrimitive(getName()));

        return jsonObject;
    }

    //TODO JavaDoc
    public static Platform fromJson(JsonObject jsonObject){
        String name = jsonObject.get("Name").getAsString();
        if (jsonObject.has("Fee")) {
            return new AbsolutePlatform(name, jsonObject.get("Fee").getAsDouble());
        } else if (jsonObject.has("Min")){
            return new MixedPlatform(name, jsonObject.get("Percent").getAsDouble(),jsonObject.get("Min").getAsDouble());
        } else {
            return new PercentPlatform(name, jsonObject.get("Percent").getAsDouble());
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {

        if(this instanceof AbsolutePlatform a) return a.equals(o);
        if(this instanceof PercentPlatform b) return b.equals(o);
        if(this instanceof MixedPlatform c) return c.equals(o);
        return false;
    }
}
