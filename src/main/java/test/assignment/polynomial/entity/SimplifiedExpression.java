package test.assignment.polynomial.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedExpression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String expression;

    @OneToMany(mappedBy = "simplified")
    private List<RawExpression> parentRawExpressions = new ArrayList<>();

    @OneToMany(mappedBy = "simplified")
    private List<Evaluation> evaluations = new ArrayList<>();

    public void addRawExpression(RawExpression rawExpression) {
        this.parentRawExpressions.add(rawExpression);
        rawExpression.setSimplified(this);
    }

    public void addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.setSimplified(this);
    }
}
