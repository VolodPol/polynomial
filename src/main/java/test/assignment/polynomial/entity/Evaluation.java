package test.assignment.polynomial.entity;


import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private double variableValue;

    @NotNull
    @Column(nullable = false)
    private double result;

    @ManyToOne(cascade = CascadeType.ALL)
    private SimplifiedExpression simplified;
}
