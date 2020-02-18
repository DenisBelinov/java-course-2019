package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.util.Map;
import java.util.Objects;

public class LinguisticSignature {
    Map<FeatureType, Double> features;

    public LinguisticSignature(Map<FeatureType, Double> features) {
        this.features = features;
    }

    public Map<FeatureType, Double> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinguisticSignature)) return false;
        LinguisticSignature that = (LinguisticSignature) o;
        return getFeatures().equals(that.getFeatures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFeatures());
    }
}
