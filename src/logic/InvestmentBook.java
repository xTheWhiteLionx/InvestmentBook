package logic;

import logic.platform.Platform;

import java.util.List;
import java.util.Objects;
import java.util.Set;

//TODO JavaDoc
public class InvestmentBook {

    //TODO JavaDoc
    private final Set<Platform> platforms;

    //TODO JavaDoc
    private final List<Investment> investmentList;

    //TODO JavaDoc
    public InvestmentBook(Set<Platform> platforms, List<Investment> investmentList){
        this.platforms = platforms;
        this.investmentList = investmentList;
    }

    //TODO JavaDoc
    public Set<Platform> getPlatforms() {
        return platforms;
    }

    //TODO JavaDoc
    public List<Investment> getInvestmentList() {
        return investmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentBook that)) return false;
        return Objects.equals(platforms, that.platforms) && Objects.equals(investmentList, that.investmentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platforms, investmentList);
    }
}
