package test.assignment.polynomial.repository;


import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseSaveRepository<T, ID> extends CrudRepository<T, ID> {

    <S extends T> S save(@NotNull S entity);
}
