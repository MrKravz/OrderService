package by.ares.orderservice.util;

import by.ares.orderservice.dto.response.ActivationStatus;
import by.ares.orderservice.model.Status;

public class TestConstants {
    public static final Long ORDER_ID = 1L;
    public static final Long ITEM_ID = 1L;
    public static final Long ORDER_ITEM_ID = 1L;
    public static final String ITEM_NAME = "Item name";
    public static final Float PRICE = 12.4F;
    public static final Status CREATED = Status.CREATED;
    public static final Status AWAITED = Status.AWAITED;
    public static final Status DONE = Status.DONE;
    public static final Long USER_ID = 1L;
    public static final Long USER_ID_2 = 1L;
    public static final String USER_NAME = "Test user";
    public static final ActivationStatus ACTIVATION_STATUS = ActivationStatus.ACTIVE;
    public static final String URI = "test_uri";
}
