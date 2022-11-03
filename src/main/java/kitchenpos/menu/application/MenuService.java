package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        return menuRepository.save(Menu.create(request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                createMenuProducts(request),
                menuValidator));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
