package com.shann.ecom.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import com.shann.ecom.dtos.*;
import com.shann.ecom.enums.OrderStatus;
import com.shann.ecom.models.*;
import com.shann.ecom.repositories.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestOrderController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderController orderController;

    private User user;
    private List<Product> products;
    private Order order;

    @BeforeEach
    public  void insertDummyData(){
        User u1 = new User();
        u1.setName("Test User");
        u1.setEmail("test@shann.com");
        user = userRepository.save(u1);


        Product product1 = new Product();
        product1.setName("Test Product 1");
        product1.setPrice(100);
        product1.setDescription("Test Description 1");
        product1 = productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setPrice(200);
        product2.setDescription("Test Description 2");
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Test Product 3");
        product3.setPrice(300);
        product3.setDescription("Test Description 3");
        product3 = productRepository.save(product3);

        products = List.of(product1, product2, product3);

        Inventory inventory1 = new Inventory();
        inventory1.setProduct(product1);
        inventory1.setQuantity(10);
        inventory1 = inventoryRepository.save(inventory1);

        Inventory inventory2 = new Inventory();
        inventory2.setProduct(product2);
        inventory2.setQuantity(10);
        inventory2 = inventoryRepository.save(inventory2);

        Inventory inventory3 = new Inventory();
        inventory3.setProduct(product3);
        inventory3.setQuantity(10);
        inventory3 = inventoryRepository.save(inventory3);


        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PLACED);
        order = orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(Product product: products){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(1);
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        this.order = orderRepository.save(order);
    }


    @AfterEach
    public void cleanUp(){
        List<Order> orders = orderRepository.findAll();
        for(Order order: orders){
            order.setOrderDetails(null);
            order.setUser(null);
            orderRepository.save(order);
        }
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void placeOrder_Success(){
        List<Inventory> beforeOrdering = inventoryRepository.findAllById(List.of(products.get(0).getId(), products.get(1).getId(), products.get(2).getId()));
        List<Pair<Integer, Integer>> orderPairs = List.of(Pair.of(products.get(0).getId(), 1), Pair.of(products.get(1).getId(), 1),
                Pair.of(products.get(2).getId(), 1));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId());
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId());
        placeOrderRequestDto.setOrderPairs(orderPairs);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.SUCCESS, placeOrderResponseDto.getStatus(), "Status should be success");
        assertEquals(OrderStatus.PLACED, placeOrderResponseDto.getOrder().getOrderStatus(), "Order status should be placed");
        assertEquals(3, placeOrderResponseDto.getOrder().getOrderDetails().size(), "There should be 3 items in the order");

        orderRepository.findById(placeOrderResponseDto.getOrder().getId()).ifPresent(order -> {
            assertEquals(user.getId(), order.getUser().getId(), "User id should be same");
            user.getAddresses().stream().filter(address -> address.getId() == order.getDeliveryAddress().getId()).findFirst().ifPresent(address -> {
                assertEquals(address.getId(), order.getDeliveryAddress().getId(), "Address id should be same");
            });
        });

        List<Inventory> afterOrdering = inventoryRepository.findAllById(List.of(products.get(0).getId(), products.get(1).getId(), products.get(2).getId()));
        for(int i = 0; i < beforeOrdering.size(); i++){
            assertEquals(beforeOrdering.get(i).getQuantity() - orderPairs.get(i).getSecond(), afterOrdering.get(i).getQuantity(), "Quantity should be updated");
        }

    }

    @Test
    public void placeOrder_IncorrectAddress_Failure(){
        List<Pair<Integer, Integer>> orderPairs = List.of(Pair.of(products.get(0).getId(), 1), Pair.of(products.get(1).getId(), 1),
                Pair.of(products.get(2).getId(), 1));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId());
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId() + 100);
        placeOrderRequestDto.setOrderPairs(orderPairs);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.FAILURE, placeOrderResponseDto.getStatus(), "Status should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");

    }

    @Test
    public void placeOrder_NonExistingUser_Failure(){
        List<Pair<Integer, Integer>> orderDetails = List.of(Pair.of(products.get(0).getId(), 1), Pair.of(products.get(1).getId(), 1),
                Pair.of(products.get(2).getId(), 1));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId() + 1000);
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId());
        placeOrderRequestDto.setOrderPairs(orderDetails);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.FAILURE, placeOrderResponseDto.getStatus(), "Status should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");

    }

    @Test
    public void placeOrder_NonExistingProduct_Failure(){
        List<Pair<Integer, Integer>> orderDetails = List.of(Pair.of(products.get(0).getId(), 1), Pair.of(products.get(1).getId(), 1),
                Pair.of(products.get(2).getId()+1000, 1));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId());
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId());
        placeOrderRequestDto.setOrderPairs(orderDetails);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.FAILURE, placeOrderResponseDto.getStatus(), "Status should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");

    }


    @Test
    public void placeOrder_OverPurchaseHighDemandProduct_Failure(){
        List<Pair<Integer, Integer>> orderDetails = List.of(Pair.of(products.get(0).getId(), 1), Pair.of(products.get(1).getId(), 1),
                Pair.of(products.get(2).getId(), 100));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId());
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId());
        placeOrderRequestDto.setOrderPairs(orderDetails);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.FAILURE, placeOrderResponseDto.getStatus(), "Status should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");

    }

    @Test
    public void placeOrder_OverPurchaseNonHighDemandProducts_Failure(){
        List<Pair<Integer, Integer>> orderDetails = List.of(Pair.of(products.get(0).getId(), 20), Pair.of(products.get(1).getId(), 15),
                Pair.of(products.get(2).getId(), 1));
        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setUserId(user.getId());
        placeOrderRequestDto.setAddressId(user.getAddresses().get(0).getId());
        placeOrderRequestDto.setOrderPairs(orderDetails);

        PlaceOrderResponseDto placeOrderResponseDto = orderController.placeOrder(placeOrderRequestDto);
        assertEquals(ResponseStatus.FAILURE, placeOrderResponseDto.getStatus(), "Status should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");

    }

    @Test
    public void testCancelOrder_Success(){
        List<Inventory> beforeOrdering = inventoryRepository.findAll();

        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(order.getId());
        requestDto.setUserId(user.getId());

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.SUCCESS, responseDto.getStatus());
        assertEquals(OrderStatus.CANCELLED, responseDto.getOrder().getOrderStatus());

        List<Inventory> afterOrdering = inventoryRepository.findAll();
        for(int i=0; i<beforeOrdering.size(); i++){
            assertEquals(beforeOrdering.get(i).getQuantity()+1, afterOrdering.get(i).getQuantity(), "Inventory quantity should be increased by 1");
        }
    }

    @Test
    public void testCancelOrder_UserNotFound(){
        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(order.getId());
        requestDto.setUserId(1000);

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.FAILURE, responseDto.getStatus());
        assertNull(responseDto.getOrder());

    }


    @Test
    public void testCancelOrder_OrderNotFound(){
        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(1000);
        requestDto.setUserId(user.getId());

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.FAILURE, responseDto.getStatus());
        assertNull(responseDto.getOrder());

    }

    @Test
    public void testCancelOrder_OrderDoesntBelongToUser() {
        User user2 = new User();
        user2.setName("Test User 2");
        user2.setEmail("test2@shann.com");
        user2 = userRepository.save(user2);

        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(order.getId());
        requestDto.setUserId(user2.getId());

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.FAILURE, responseDto.getStatus());
        assertNull(responseDto.getOrder());
    }

    @Test
    public void testCancelOrder_OrderAlreadyShipped(){
        order.setOrderStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(order.getId());
        requestDto.setUserId(user.getId());

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.FAILURE, responseDto.getStatus());
        assertNull(responseDto.getOrder());
    }

    @Test
    public void testCancelOrder_OrderAlreadyDelivered(){
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        CancelOrderRequestDto requestDto = new CancelOrderRequestDto();
        requestDto.setOrderId(order.getId());
        requestDto.setUserId(user.getId());

        CancelOrderResponseDto responseDto = orderController.cancelOrder(requestDto);
        assertEquals(ResponseStatus.FAILURE, responseDto.getStatus());
        assertNull(responseDto.getOrder());
    }

}
