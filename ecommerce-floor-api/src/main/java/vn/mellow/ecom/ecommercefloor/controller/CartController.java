package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.controller.controller.CartDetailController;
import vn.mellow.ecom.ecommercefloor.controller.controller.CreateCartController;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.cart.CartDetail;
import vn.mellow.ecom.ecommercefloor.model.cart.CartItem;
import vn.mellow.ecom.ecommercefloor.model.input.CreateCartInput;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cart/1.0.0/")
public class CartController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private CreateCartController createCartController;
    @Autowired
    private CartDetailController cartDetailController;

    @ApiOperation(value = "create a new cart")
    @PostMapping("/cart/create")
    public Cart createCart(@RequestBody CreateCartInput cartInput) throws ServiceException {
        return createCartController.createCart(cartInput.getCart(), cartInput.getCartItemList());
    }

    @ApiOperation(value = "create a new cart item")
    @PostMapping("/cart-item/create")
    public CartItem createCartItem(@RequestBody CartItem cartItem) throws ServiceException {
        return createCartController.createCartItem(cartItem);
    }

    @ApiOperation(value = "get cart detail by cart id")
    @GetMapping("/cart/{userId}/detail")
    public CartDetail getCartDetail(@PathVariable String userId) throws ServiceException {
        return cartDetailController.getCartDetail(userId);
    }

    @ApiOperation(value = "deleted cart item by cart item id")
    @DeleteMapping("/cart-item/{cartItemId}/deleted")
    public CartDetail deletedCartItem(@PathVariable String cartItemId) throws ServiceException {
        return cartDetailController.deleteCartItem(cartItemId);
    }

    @ApiOperation(value = "update quantity cart item by cart item id")
    @PutMapping("/cart-item/{cartItemId}/update-quantity")
    public CartDetail updateQuantityCartItem(@PathVariable String cartItemId, @RequestParam("quantity") long quantity) throws ServiceException {
        return cartDetailController.updateQuantityCartItem(cartItemId, quantity);
    }


    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }

}
