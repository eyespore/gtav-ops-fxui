package club.pineclone.gtavops.domain.dto.macro;

public record PercentageDTO(Double percentage, Double min, Double max) {

    public double parseDouble() {
        return min + percentage * (max - min);
    }

    public long parseLong() {
        return (long) Math.floor(parseDouble());
    }

    public static PercentageDTO ofValue(double value, double min, double max) {
        double pct = (value - min) / (max - min);
        return new PercentageDTO(pct, min, max);
    }

    public static PercentageDTO fromRealValue(long value, double min, double max) {
        return ofValue(value, min, max);
    }

}
