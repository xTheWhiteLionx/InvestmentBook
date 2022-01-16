package logic;

import logic.platform.Platform;

import java.util.List;
import java.util.Set;

/**
 * Interface used for the logic of the game Labyrinth to communicate with the
 * gui.
 *
 * @author Fabian
 */
//TODO JavaDoc
public interface GUIConnector {

    /**
     * Displays what should the player have to do next,
     * depending of a row or a column has already been pushed.
     *
     * @param investments
     */
    //TODO JavaDoc
    void displayInvestmentList(List<Investment> investments);

    /**
     * Displays what should the player have to do next,
     * depending of a row or a column has already been pushed.
     *
     * @param
     */
    //TODO JavaDoc
    void displayPlatforms(Set<Platform> platforms);

    /**
     * Displays what should the player have to do next,
     * depending of a row or a column has already been pushed.
     *
     * @param investment
     */
    //TODO JavaDoc
    void addInvestmentOnDisplay(Investment investment);

    /**
     * Displays what should the player have to do next,
     * depending of a row or a column has already been pushed.
     *
     * @param newPlatform
     */
    //TODO JavaDoc
    void addPlatform(Platform newPlatform);

    //TODO JavaDoc
    void deletePlatform(Platform platform);

}
