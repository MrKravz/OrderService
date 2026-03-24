package by.ares.orderservice.service;

public interface StatusChangerService<T, R> {
    T changeStatus(T id, R status);
}
