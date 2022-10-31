package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderUpdateRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));
        validateOrderTableEmpty(orderTable);

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING, createOrderLineItems(request));
        return orderRepository.save(order);
    }

    private void validateOrderTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException("해당 OrderTable이 empty 상태 입니다.");
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Order 입니다."));

        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return savedOrder;
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
        return new OrderLineItem(menu.getId(), request.getQuantity());
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(it -> toOrderLineItem(it))
                .collect(Collectors.toList());
    }
}
