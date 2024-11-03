package test.assignment.polynomial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
//@Table("raw_expression")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawExpression implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column("expression")
    private String expression;
}
