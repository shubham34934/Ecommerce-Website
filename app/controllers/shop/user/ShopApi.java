package controllers.shop.user;

import helper.datasources.MorphiaObject;
import models.Cart;
import models.Checkout;
import models.Product;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.InventoryService;
import services.ProductService;
import services.SearchService;
import views.html.shop.checkout;


public class ShopApi extends Controller {

    public Result products(String category) {
        return ok(ProductService.getProducts(category));
    }

    public Result featuredItem() {
        return ok(ProductService.featuredItem());
    }

    public Result productDetail(String sku) {
        return ok(ProductService.getProduct(sku));
    }


    public Result search(String key) {
        return ok( SearchService.search(key) );
    }

    public Result addToCart() {
        try {
            final Form<Cart.CartItem> filledForm = Cart.CartItem._FORM.bindFromRequest();
            final Cart.CartItem model = filledForm.get();
//            final DynamicForm dynamicForm = Form.form().bindFromRequest();
//            final String userId = dynamicForm.get("userId");
            String userId = request().cookie("userId").value();
            InventoryService.addItemToCart(userId,model);
        } catch (Exception e) {
            return badRequest("Out of Stock.");
        }
        return ok("Done"); //some json with success, for android app;
        //return redirect(routes.ProductBuy.cart());
    }

    public Result removeCompleteCart() {
        String userId = request().cookie("userId").value();
        MorphiaObject.datastore.delete( Cart.findByUserId(userId));

        //TODO TEMP MANJEET
        //return ok(""); //some json with success, for android app;
        return redirect(routes.ProductBuy.cart());
    }

    public Result getCartCount() {
        String userId = request().cookie("userId").value();
        //Logger.info("user id from cookie="+userId);
        return ok(InventoryService.getCartCount(userId));
    }

    public Result getCartDetail() {
        String userId = request().cookie("userId").value();
        //Logger.info("user id from cookie="+userId);
        return ok(InventoryService.getCartDetail(userId));
    }

    public Result checkoutCart() {
        try {
            final Form<Checkout> filledForm = Checkout._FORM.bindFromRequest();
            final Checkout model = filledForm.get();
            InventoryService.checkoutCart(model);
        } catch (Exception e) {
            return badRequest("Cart is Empty.");
        }
        return ok("done"); //some json with success, for android app;
       // return redirect(routes.ProductBuy.cart());
    }

    public Result categoryTree() {
        return ok(ProductService.getCategoryTree());
    }

    public Result categoryLeaf() {
        return ok(ProductService.getCategoryLeaf());
    }
}
