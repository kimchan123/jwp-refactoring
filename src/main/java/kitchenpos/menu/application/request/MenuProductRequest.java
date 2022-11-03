package kitchenpos.menu.application.request;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
