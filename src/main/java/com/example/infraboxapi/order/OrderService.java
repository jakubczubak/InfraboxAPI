package com.example.infraboxapi.order;
import com.example.infraboxapi.material.MaterialRepository;
import com.example.infraboxapi.notification.NotificationDescription;
import com.example.infraboxapi.notification.NotificationService;
import com.example.infraboxapi.orderItem.OrderItem;
import com.example.infraboxapi.orderItem.OrderItemDTO;
import com.example.infraboxapi.tool.ToolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MaterialRepository materialRepository;
    private final ToolRepository toolRepository;
    private final NotificationService notificationService;
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    public void addOrder(OrderDTO orderDTO) {


        System.out.println(orderDTO);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            OrderItem orderItem = OrderItem.builder()

                    .name(orderItemDTO.getName())
                    .quantity(orderItemDTO.getQuantity())
                    .material(materialRepository.findById(orderItemDTO.getItemID()).orElse(null))
                    .tool(toolRepository.findById(orderItemDTO.getItemID()).orElse(null))
                    .build();
            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .name(orderDTO.getName())
                .date(orderDTO.getDate())
                .status(orderDTO.getStatus())
                .supplierEmail(orderDTO.getSupplierEmail())
                .supplierMessage(orderDTO.getSupplierMessage())
                .isAddedToWarehouse(false)
                .isQuantityInTransportSet(false)
                .totalPrice(orderDTO.getTotalPrice())
                .orderItems(orderItems)
                .build();



        orderRepository.save(order);

        notificationService.createAndSendNotification("Order  " + order.getName() + " has been added.", NotificationDescription.OrderAdded);


    }
}
