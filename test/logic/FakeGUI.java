package logic;

import logic.platform.Platform;

import java.util.List;
import java.util.Set;

/**
 * Fake GUI used for testing the logic of the game Tic Tac Toe. All methods do
 * nothing. To ensure that the logic calls the correct methods for the gui, it
 * could be possibly to add package private boolean attributes, that tell if a
 * certain method has been called.
 *
 * @author Fabian
 */
//TODO JavaDoc
public class FakeGUI implements GUIConnector {

    @Override
    public void displayInvestmentList(List<Investment> investments) {

    }

    @Override
    public void displayPlatforms(Set<Platform> platforms) {

    }

    @Override
    public void addInvestmentOnDisplay(Investment investment) {

    }

    @Override
    public void addPlatform(Platform newPlatform) {

    }

    @Override
    public void deletePlatform(Platform platform) {

    }

}
