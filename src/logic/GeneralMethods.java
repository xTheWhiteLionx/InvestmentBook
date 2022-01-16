package logic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import gui.Main;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.platform.Platform;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Math.round;
import static javafx.scene.paint.Color.*;

/**
 * Hello There! This class contains the general variables and Methods.
 *
 * @author Fabian Hardt
 */
public class GeneralMethods {

    //TODO JavaDoc
    public static final Image ICON = new Image("gui/textures/investmentBookIcon.png");

    /**
     * regex for only integers or double values
     * (only two decimal places are guaranteed)
     */
    public static final String TXT_FIELD_REGEX = "^[0-9]+([,.][0-9][0-9])?$";

    /**
     * StringArray of Attributes of an Investment
     */
    //TODO JavaDoc
    public static final String[] ATTRIBUTES = {"creationDate", "status", "platform", "stockName", "exchangeRate", "capital",
            "sellingPrice", "absolutePerformance", "percentPerformance", "cost"};

    /**
     * Currency of the Investments
     */
    public static final Currency CURRENCY = Currency.getInstance(Locale.getDefault());

    //TODO JavaDoc
    /**
     *
     */
    public static final String SYMBOL_OF_CURRENCY = CURRENCY.getSymbol();

    /**
     * Length of the columns must be 20 or higher
     */
    protected static final int COL_LENGTH = 20; //min. 19 (length of the shortest line)

    /**
     * The directory of the files
     */
    //TODO delete and pull it to the write/read methode
    public static final String DIRECTORY = "books/";

    public static void setCurrenciesForLbls(Label[] lbls) {
        for (Label lbl : lbls) {
            lbl.setText(SYMBOL_OF_CURRENCY);
        }
    }

    //TODO JavaDoc
    public static <T> T createStage(String fxmlPath, String title, int width, int height) {
        final Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(fxmlPath)));

        newStage.getIcons().add(ICON);
        newStage.setTitle(title);
        newStage.setMinWidth(width);
        newStage.setMinHeight(height);
        newStage.initModality(Modality.APPLICATION_MODAL);
        try {
            newStage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.show();

        return loader.getController();
    }

    //TODO JavaDoc
    public static boolean isValidDouble(TextField textField) {
        assert textField != null;
        return textField.getText().matches(TXT_FIELD_REGEX);
    }

    public static boolean areValidDoubles(TextField[] textFields) {
        boolean areValidDoubles = true;
        for (int i = 0; i < textFields.length && areValidDoubles; i++) {
            areValidDoubles = isValidDouble(textFields[i]);
        }
        return areValidDoubles;
    }

    //TODO JavaDoc
    public static <T> ChangeListener<T> createChangeListener(TextField textField, Button button) {
        assert textField != null;
        assert button != null;
        return (observable, oldValue, newValue) -> {
            //regex for only integers or double values
            // (only two decimal places are guaranteed)
            button.setDisable(!isValidDouble(textField));
        };
    }

    public static <T> ChangeListener<T> createChangeListener(DatePicker creationDatePicker,
                                                             TextField stockNameTxtFld,
                                                             TextField exchangeRateTxtFld,
                                                             TextField capitalTxtFld,
                                                             TextField sellingPriceTxtFld,
                                                             DatePicker sellingDatePicker,
                                                             Button button) {
        assert stockNameTxtFld != null;
        assert exchangeRateTxtFld != null;
        assert capitalTxtFld != null;
        assert sellingPriceTxtFld != null;
        assert button != null;
        return (observable, oldValue, newValue) -> {
            //regex for only integers or double values
            // (only two decimal places are guaranteed)
            boolean someInputIsInvalid = creationDatePicker == null
                    || stockNameTxtFld.getText().isEmpty()
                    || !isValidDouble(exchangeRateTxtFld)
                    || !isValidDouble(capitalTxtFld)
                    //To be a valid investment the sellingDate and sellingPrice has to be both
                    // invalid or both attributes has to be valid
                    || (sellingDatePicker.getValue() != null ^ isValidDouble(sellingPriceTxtFld));

            button.setDisable(someInputIsInvalid);
        };
    }

    //TODO JavaDoc
    public static double doubleOfTextField(TextField textField) {
        assert textField != null;
        return Double.parseDouble(textField.getText().replace(",", "."));
    }

    //TODO JavaDoc
    public static String stringOfDouble(double number) {
        return String.valueOf(number).replace(".", ",");
    }

    //TODO JavaDoc
    public static void setAndColorText(double value, Label valueLbl) {
        valueLbl.setText(stringOfDouble(value));
        Paint color = BLACK;
        if (value > 0) {
            color = GREEN;
        } else if (value < 0) {
            color = RED;
        }
        valueLbl.setTextFill(color);
    }

    /**
     * Filled a string with spaces until colLength is reached depending
     * on the given sentenceLength
     *
     * @param sentenceLength the given length of a sentence
     * @return filled string with spaces
     */
    public static String calcSpaces(int sentenceLength) {
        int length = 0;

        if (sentenceLength > 0) {
            if (COL_LENGTH > sentenceLength) {
                length = COL_LENGTH - sentenceLength;
            }
            return " ".repeat(length);
        } else {
            throw new IllegalArgumentException("sentence has negative or no length?");
        }
    }

    /**
     * @param d
     * @return
     */
    //TODO JavaDoc
    public static double roundDouble(double d) {
        //TODO?
//        return d <= 0.01 ? d : round(d * 100d) / 100d;
        return round(d * 100d) / 100d;
    }

    /**
     * Calculates the percent of the given price depending on the given base (100%).
     * rule of three.
     *
     * @param base  the given base (100%)
     * @param price the given price
     * @return percent of the given price
     */
    public static double calcPercent(double base, double price) {
        return (100d / base) * price;
    }

    /**
     * Calculates the percent of the given price depending on the given base (100%),
     * with calling the methode calcPercent
     *
     * @param base  the given base (100%)
     * @param price the given price
     * @return percent of the given price
     */
    //TODO JavaDoc
    public static double calcPercentRounded(double base, double price) {
        return roundDouble(calcPercent(base, price));
    }

    /**
     * Writes a Json File with the information of the given investments named by the given file name
     *
     * @param investmentBook the given investments
     * @param fileName       given file name
     */
    //TODO JavaDoc
    //TODO delete
    public static void writeInvestmentBookToJson(InvestmentBook investmentBook, String fileName) {
        if (fileName != null && investmentBook != null) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    //serializer to parse the LocalDates
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, jsonSerializationContext) -> src == null ? null : new JsonPrimitive(src.toEpochDay()))
                    //serializer to parse the Platform
                    .registerTypeAdapter(Platform.class, (JsonSerializer<Platform>) (src, type, jsonSerializationContext) -> src == null ? null : src.toJson())
                    .create();

            try (FileWriter writer = new FileWriter(DIRECTORY + fileName)) {
                gson.toJson(investmentBook, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("one or more param was null");
        }
    }

    public static void writeInvestmentBookToJson(InvestmentBook investmentBook, File file) {
        assert investmentBook != null;
        assert file != null;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                //serializer to parse the LocalDates
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, jsonSerializationContext) -> src == null ? null : new JsonPrimitive(src.toEpochDay()))
                //serializer to parse the Platform
                .registerTypeAdapter(Platform.class,
                        (JsonSerializer<Platform>) (src, type, jsonSerializationContext) -> src == null ? null : src.toJson())
                .create();

        try (FileWriter writer = new FileWriter(file.getAbsoluteFile())) {
            gson.toJson(investmentBook, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the given filename and returns investment(s)
     *
     * @param filename the given filename
     * @return the investments of the (Json) File
     */
    //TODO JavaDoc
    //TODO delete
    public static InvestmentBook readInvestmentBookFromJson(String filename) {
        if (filename != null) {
            Gson gson = new GsonBuilder()
                    //Deserializer to parse the dates as LocalDates
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                            (json, type, jsonDeserializationContext) -> json == null ? null : LocalDate.ofEpochDay(json.getAsLong()))
                    .registerTypeAdapter(Platform.class, (JsonDeserializer<Platform>)
                            (json, type, jsonDeserializationContext) -> json == null ? null : Platform.fromJson(json.getAsJsonObject()))
                    .create();

            Type listType = new TypeToken<InvestmentBook>() {
            }.getType();

            if (filename.contains(".json")) {
                System.out.println(filename);
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(DIRECTORY + filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if (Objects.requireNonNull(fileReader).ready()) {
                        JsonReader reader = new JsonReader(fileReader);

                        return gson.fromJson(reader, listType);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("the file must has to be an json file");
            }
        } else {
            throw new IllegalArgumentException("given fileName is null");
        }
        return null;
    }

    public static InvestmentBook readInvestmentBookFromJson(File file) {
        Gson gson = new GsonBuilder()
                //Deserializer to parse the dates as LocalDates
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, jsonDeserializationContext) -> json == null ? null : LocalDate.ofEpochDay(json.getAsLong()))
                .registerTypeAdapter(Platform.class, (JsonDeserializer<Platform>)
                        (json, type, jsonDeserializationContext) -> json == null ? null : Platform.fromJson(json.getAsJsonObject()))
                .create();

        Type listType = new TypeToken<InvestmentBook>() {
        }.getType();

        if (file.getName().contains(".json")) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file.getAbsoluteFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (Objects.requireNonNull(fileReader).ready()) {
                    JsonReader reader = new JsonReader(fileReader);

                    return gson.fromJson(reader, listType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("the file must has to be an json file");
        }
        return null;
    }

//    /**
//     * Reads the given filename and returns investment(s)
//     *
//     * @param filename the given filename
//     * @return the investments of the (Json) File
//     * @throws IOException
//     */
//    //TODO delete?
//    public static List<Investment> readInvestsJson(String directory, String filename) throws IOException {
//        if (filename != null) {
//            Gson gson = new GsonBuilder()
//                    //Deserializer to parse the dates as LocalDates
//                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
//                            (json, type, jsonDeserializationContext) -> json == null ? null : LocalDate.ofEpochDay(json.getAsLong()))
//                    .registerTypeAdapter(Platform.class, (JsonDeserializer<Platform>)
//                            (json, type, jsonDeserializationContext) -> json == null ? null : Platform.fromJson(json.getAsJsonObject()))
//                    .create();
//
//            Type listType = new TypeToken<ArrayList<Investment>>() {
//            }.getType();
//
//            if (filename.contains(".json")) {
//                System.out.println(filename);
//                FileReader fileReader = new FileReader(directory + filename);
//                if (fileReader.ready()) {
//                    JsonReader reader = new JsonReader(fileReader);
//
//                    return gson.fromJson(reader, listType);
//                } else {
//                    throw new FileNotFoundException("the file doesn't exist");
//                }
//            } else {
//                throw new IllegalArgumentException("the file must has to be an json file");
//            }
//        } else {
//            throw new IllegalArgumentException("given fileName is null");
//        }
//    }

//    /**
//     * Reads the given (Json) File and returns investment(s)
//     *
//     * @param file the given (Json) File
//     * @return the investments of the (Json) File
//     * @throws IOException
//     */
//    //TODO delete?
//    public static List<Investment> readInvestsJson(File file) throws IOException {
//        if (file != null) {
//            return readInvestsJson("/books", file.getName());
//        } else {
//            throw new IllegalArgumentException("given file is null");
//        }
//    }

}
