package logic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import gui.DialogWindow;
import logic.platform.Platform;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

/**
 * This class contains {@code InvestmentBookData} with all his operations.
 * An instance of the InvestmentBook class can be created, which contains all the
 * information from a JSON-File and checks it for any errors.
 * <p>
 * Whereupon a new instance of the {@link InvestmentBook} can be created.
 * This applies to both directions, so this class acts as a transmitter class between
 * JSON-Files (save/load game) and the logic.
 *
 * @author xthe_white_lionx
 */
public class InvestmentBookData {

    /** Platforms of the InvestmentBookData */
    private final Set<Platform> platforms;

    /** Investments of the InvestmentBookData */
    private final List<Investment> investments;

    /**
     * Constructor for an investmentBookData.
     *
     * @param platforms
     * @param investments
     */
    //TODO JavaDoc
    public InvestmentBookData(Set<Platform> platforms, List<Investment> investments){
        if (platforms == null) {
            throw new NullPointerException("platforms == null");
        }
        if (investments == null) {
            throw new NullPointerException("investments == null");
        }

        this.platforms = platforms;
        this.investments = investments;
    }

    //TODO JavaDoc
    public InvestmentBookData(InvestmentBook investmentBook){
        this(investmentBook.getPlatforms(), investmentBook.getInvestments());
    }

    //TODO JavaDoc
    public static InvestmentBookData fromJson(File file) {
        Gson gson = new GsonBuilder()
                //Deserializer to parse the dates as LocalDates
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, jsonDeserializationContext) -> json == null ? null :
                                LocalDate.ofEpochDay(json.getAsLong()))
                .registerTypeAdapter(Platform.class, (JsonDeserializer<Platform>)
                        (json, type, jsonDeserializationContext) -> json == null ? null :
                                Platform.fromJson(json.getAsJsonObject()))
                .create();

        Type listType = new TypeToken<InvestmentBookData>() {
        }.getType();

        if (file.getName().contains(".json")) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                DialogWindow.ExceptionAlert(e);
                e.printStackTrace();
            }
            try {
                if (fileReader != null && fileReader.ready()) {
                    JsonReader reader = new JsonReader(fileReader);

                    return gson.fromJson(reader, listType);
                }
            } catch (IOException e) {
                DialogWindow.ExceptionAlert(e);
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("the file must has to be an json file");
        }
        return null;
    }

    /**
     *
     * @return
     */
    //TODO JavaDoc
    public Set<Platform> getPlatforms() {
        return new HashSet<>(platforms);
    }

    /**
     *
     * @return
     */
    //TODO JavaDoc
    public List<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }

    /**
     *
     * @param file
     */
    //TODO JavaDoc
    public void toJson(File file) {
        assert file != null;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                //serializer to parse the LocalDates
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, jsonSerializationContext) ->
                                src == null ? null : new JsonPrimitive(src.toEpochDay()))
                //serializer to parse the Platform
                .registerTypeAdapter(Platform.class,
                        (JsonSerializer<Platform>) (src, type, jsonSerializationContext) ->
                                src == null ? null : src.toJson())
                .create();

        try (FileWriter writer = new FileWriter(file.getAbsoluteFile())) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            DialogWindow.ExceptionAlert(e);
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "InvestmentBookData{" +
                "platforms=" + platforms +
                ", investments=" + investments +
                '}';
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentBookData that)) return false;
        return Objects.equals(platforms, that.platforms) && Objects.equals(investments, that.investments);
    }
}
