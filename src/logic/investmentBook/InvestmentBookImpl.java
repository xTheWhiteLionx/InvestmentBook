package logic.investmentBook;

import logic.Investment;
import logic.platform.Platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class InvestmentBookImpl {
    /**
     * Platforms of the InvestmentBook
     */
    final Set<Platform> platforms;

    /**
     * Investments of the InvestmentBook
     */
    final List<Investment> investments;

    /**
     *
     * @param platforms
     * @param investments
     */
    public InvestmentBookImpl(Set<Platform> platforms, List<Investment> investments) {
        if (platforms == null) {
            throw new NullPointerException();
        }
        if (investments == null) {
            throw new NullPointerException();
        }

        this.platforms = platforms;
        Collections.sort(investments);
        this.investments = investments;
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
        if (!(o instanceof InvestmentBookImpl that)) return false;
        return Objects.equals(platforms, that.platforms)
                && Objects.equals(investments, that.investments);
    }
}
