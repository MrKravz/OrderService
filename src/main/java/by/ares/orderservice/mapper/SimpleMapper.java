package by.ares.orderservice.mapper;

public interface SimpleMapper<R, D, E> {
    E toModel(R request);
    D toDto(E entity);
}
