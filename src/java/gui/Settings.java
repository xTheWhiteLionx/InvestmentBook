package gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Settings.class);

    private static final String FILE = "src/resources/gui/settings/Settings.xml";

    private static final String AUTO_SAVE = "Autosave";

    private static final String LIGHT_MODE = "Light mode";

    private static void savePreferences(){
        try {
            PREFERENCES.exportNode(new FileOutputStream(FILE));
        } catch (IOException | BackingStoreException e) {
            DialogWindow.displayError(e);
        }
    }

    public static boolean isAutoSave() {
        return PREFERENCES.getBoolean(AUTO_SAVE, false);
    }

    public static void setAutoSave(boolean autoSave) {
        PREFERENCES.putBoolean(AUTO_SAVE, autoSave);
        savePreferences();
    }

    public static boolean isLightMode() {
        return PREFERENCES.getBoolean(LIGHT_MODE, true);
    }

    public static void setLightMode(boolean isLightMode) {
        PREFERENCES.putBoolean(LIGHT_MODE, isLightMode);
        savePreferences();
    }

}
