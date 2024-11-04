package test.assignment.polynomial.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<RawExpression> parentRawExpressions;

    @OneToMany(mappedBy = "simplified")
    private List<Evaluation> evaluations;
}
