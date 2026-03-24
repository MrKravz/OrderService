package by.ares.orderservice.service;


public interface CrudService<T, R, I> {
    T findById(I id);
    I save(R orderRequest);
    I update(R orderRequest, I id);
    void delete(I id);
}
