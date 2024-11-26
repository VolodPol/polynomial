package test.assignment.polynomial.service.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Expression {
    private List<Polynomial> multipliers;

    public Expression() {
        multipliers = new ArrayList<>();
    }

    public void addMultiplier(Polynomial multiplier) {
        multipliers.add(multiplier);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Polynomial {
        private List<Additive> additives;

        public Polynomial() {
            additives = new ArrayList<>();
        }

        public void addAdditive(Additive additive) {
            additives.add(additive);
        }

        @AllArgsConstructor
        @Getter @Setter
        @EqualsAndHashCode
        public static class Additive {
            private Integer coefficient;
            private int exponent;
        }
    }
}
