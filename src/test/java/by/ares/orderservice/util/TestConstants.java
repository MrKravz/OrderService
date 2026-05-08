package by.ares.orderservice.util;

import by.ares.orderservice.dto.response.ActivationStatus;
import by.ares.orderservice.model.PaymentStatus;
import by.ares.orderservice.model.Status;

public class TestConstants {
    public static final Long ORDER_ID = 1L;
    public static final Long ITEM_ID = 1L;
    public static final String ITEM_NAME = "Item name";
    public static final String UPDATED_ITEM_NAME = "Updated name";
    public static final Long PRICE = 124L;
    public static final Status AWAITED = Status.WAITING;
    public static final Status CONFIRMED = Status.CONFIRMED;
    public static final PaymentStatus SUCCESS = PaymentStatus.SUCCESS;
    public static final PaymentStatus FAILED = PaymentStatus.FAILED;
    public static final Long USER_ID = 1L;
    public static final Long USER_ID_2 = 2L;
    public static final String USER_NAME = "Test user";
    public static final ActivationStatus ACTIVATION_STATUS = ActivationStatus.ACTIVE;
    public static final String URI = "test_uri";
}
