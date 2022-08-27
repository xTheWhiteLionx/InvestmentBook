package gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Settings.class);

    private static final String MODE_KEY = "Style";

    private static final String AUTOSAVE = "Autosave";

    public static void setMode(String cssFile) {
        PREFERENCES.put(MODE_KEY, cssFile);
        try {
            PREFERENCES.exportNode(new FileOutputStream("src/resources/gui/settings/" +
                    "Preferences.xml"));
        } catch (IOException | BackingStoreException e) {
            DialogWindow.displayError(e);
        }
    }
    public static String getMode() {
        return PREFERENCES.get(MODE_KEY, "");
    }

    public static boolean isAutoSave() {
        return PREFERENCES.getBoolean(AUTOSAVE, true);
    }

    public static void setAutoSave(boolean autoSave) {
        PREFERENCES.putBoolean(AUTOSAVE, autoSave);
    }
}
