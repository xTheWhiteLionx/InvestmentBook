package logic.platform;

/**
 * Enum of fee types which a platform could have
 *
 * @author xthe_white_lionx
 */
public enum FeeType {
    PERCENT, ABSOLUTE, MIXED;

    public String getName(){
        return this.name().toLowerCase();
    }
}
