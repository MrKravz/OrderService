package by.ares.orderservice.service;

public interface MessageReceiverService<T> {
    void listen(T t);
}
