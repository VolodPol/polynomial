package test.assignment.polynomial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "raw_expression")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawExpression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String expression;

    @ManyToOne(cascade = CascadeType.ALL)
    private SimplifiedExpression simplified;
}
