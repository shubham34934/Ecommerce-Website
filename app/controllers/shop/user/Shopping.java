package controllers.shop.user;

import models.Cart;
import models.Product;
import play.*;
import play.data.Form;
import play.mvc.*;

import services.ProductService;
import services.SearchService;
import views.html.shop.*;

public class Shopping extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public Result products(final String category, final boolean islist) {
        if(islist)
            return ok(product_list.render(category,ProductService.getProducts(category)));
        else
        return ok(product_grid.render(category,ProductService.getProducts(category)));
    }


    public Result product(String sku) {
        return ok(single_product.render(Cart.CartItem._FORM, ProductService.getProduct(sku)));
    }

    public Result blog() {
        return ok(blog.render("Your new application is ready."));
    }

    public Result search(final String key) {
        String searchKey = key;
        searchKey = Form.form().bindFromRequest().get("key");
        Logger.info("key "+searchKey);
        return ok( product_search.render(SearchService.search(searchKey)) );
    }

}
