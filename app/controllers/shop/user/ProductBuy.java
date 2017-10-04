package controllers.shop.user;

import models.Checkout;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.InventoryService;
import views.html.shop.checkout;
import views.html.shop.index;
import views.html.shop.product_grid;
import views.html.shop.shopping_cart;

public class ProductBuy extends Controller {

    public Result cart() {
        String userId = request().cookie("userId").value();
        Logger.info("user id from cookie=" + userId);
        return ok(shopping_cart.render(InventoryService.getCartDetail(userId)));
    }


//    public Result addToCart(String sku) {
//        return ok(shopping_cart.render("Your new application is ready."));
//    }
//
//    public Result addToWishList(String sku) {
//        return ok(shopping_cart.render("Your new application is ready."));
//    }


    public Result checkout() {
        String userId = request().cookie("userId").value();
        return ok(  checkout.render ( Checkout._FORM, InventoryService.getCartDetail(userId) )   );
    }

}
