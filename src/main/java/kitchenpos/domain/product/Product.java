package kitchenpos.domain.product;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private ProductPrice productPrice;

    protected Product() {
    }

    public Product(final Long id, final String name, final ProductPrice productPrice) {
        this.id = id;
        this.name = name;
        this.productPrice = productPrice;
    }

    public Product(final String name, final ProductPrice productPrice) {
        this(null, name, productPrice);
    }

    public BigDecimal multiplyQuantity(final long quantity) {
        return productPrice.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}
