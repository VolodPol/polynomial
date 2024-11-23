package test.assignment.polynomial.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
