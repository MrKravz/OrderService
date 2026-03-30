package by.ares.orderservice.service;


public interface CrudService<T, R, I> {
    T findById(I id);
    I save(R request);
    I update(R request, I id);
    void delete(I id);
}
