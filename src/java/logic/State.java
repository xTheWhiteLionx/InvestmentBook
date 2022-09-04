package logic;

/**
 * Enum of status which an {@link Investment} could reach.
 *
 * @author xthe_white_lionx
 */
public enum State {
    OPEN, CLOSED;

    /**
     * Returns
     *
     * @return
     */
    public boolean isOpen(){
        return this == OPEN;
    }
}


