package test.assignment.polynomial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
//@Table("raw_expression")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawExpression implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String expression;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private SimplifiedExpression simplified;
}
