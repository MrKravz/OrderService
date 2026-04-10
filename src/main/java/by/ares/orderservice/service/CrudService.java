package by.ares.orderservice.service;


public interface CrudService<T, R, I> {
    T findById(I id);
    T save(R request);
    T update(R request, I id);
    void delete(I id);
}
