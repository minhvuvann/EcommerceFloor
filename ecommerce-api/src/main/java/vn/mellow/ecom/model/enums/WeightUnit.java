package vn.mellow.ecom.model.enums;
public enum WeightUnit {
    KILOGRAMS("KILOGRAMS"),
    GRAMS("GRAMS"),
    POUNDS("POUNDS"),
    OUNCES("OUNCES");

    private final String graphqlName;

    private WeightUnit(String graphqlName) {
        this.graphqlName = graphqlName;
    }

    public String toString() {
        return this.graphqlName;
    }
}
