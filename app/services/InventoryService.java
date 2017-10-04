package services;

import helper.datasources.MorphiaObject;
import models.Cart;
import models.Checkout;
import models.DeliveryTracking;
import models.Product;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import play.Logger;
import utils.notify.MailSender;
import utils.notify.MailSenderParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static play.libs.Json.stringify;
import static play.libs.Json.toJson;

//http://docs.mongodb.org/ecosystem/use-cases/inventory-management/
//http://web.archive.org/web/20110713174947/http://kylebanker.com/blog/2010/04/30/mongodb-and-ecommerce/
//http://www.infoq.com/articles/data-model-mongodb


public class InventoryService {

    public static boolean isAvailable(Cart.CartItem cartItem) {
        if(cartItem==null) return false;
        Product product = MorphiaObject.datastore.find(Product.class, "sku =", cartItem.sku).get();
        if(product.qty >= cartItem.qty)
            return true;
        else return false;
    }

    //THIS USER ID SHOULD BE TAKEN FROM SESSTION/AUTH PLUGIN
    //check if this userid doesn't have any cart then create one.
   // public static void addItemToCart(ObjectId cartId, String sku, int qty) {
   public static void addItemToCart(String userId, Cart.CartItem cartItem) throws Exception {
       try {
           if(!isAvailable(cartItem)) {
               throw new Exception("Out of Stock");
           }
        Cart cart = Cart.findByUserId(userId);
        Cart.CartItem item = null;
        int qty_delta = cartItem.qty;

        if(cart == null) {
            Logger.info("Creating new cart for this user");
            // if there is no active cartId for the current user
            cart = new Cart();
            cart.userId = userId;
            cart.last_modified = new Date();
            cart.status = Cart.CartStatus.ACTIVE;
            item = cartItem;
            item.repopulateItemDetail();
            cart.items.add(item);
        } else {

            // if cartid present
            //cart = Cart.findById(cartId);
            //cart = MorphiaObject.datastore.find(Cart.class, "_id =", cartId).get();
            //cart = MorphiaObject.datastore.createQuery(Cart.class).field("_id").equal(cartId).get();
            cart.status = Cart.CartStatus.ACTIVE;


            ///TODO: if client has just increment count then update same sku,
            // but if client has added same product in the cart again then add new sku.
            // this information can be taken from the client post entry
            boolean flagUpdateOld = false; // for now always add as new one.

            List<Cart.CartItem> allItems = cart.items.stream().filter(i -> i.sku == cartItem.sku).collect(Collectors.toList());
            if (allItems != null && allItems.size() > 0)
                item = allItems.get(0);

            //if this sku is already not there create one
            if (item == null || !flagUpdateOld ) {
                item = cartItem;
                item.repopulateItemDetail();
                cart.items.add(item);
            } else {
                //other wise just update qty
                qty_delta = cartItem.qty - item.qty;
                item.qty = cartItem.qty;
            }

        }

        MorphiaObject.datastore.save(cart);

        // if everything goes ok
        Product product = MorphiaObject.datastore.find(Product.class, "sku =", item.sku).get();
        Logger.info("Old qty=" + product.qty + ", delta qty=" + qty_delta);
        product.qty = product.qty - qty_delta;
        if(product.qty >=0) {
            // TODO updated in_carted
            Logger.info("new qty="+product.qty+", delta qty="+qty_delta);
            MorphiaObject.datastore.save(product);
        }
        else {
            //if inventory update fails ,,, revert Cart change
            cart.items.remove(item);
            MorphiaObject.datastore.save(cart);
            throw new Exception("out of stock");
        }

       } catch(Exception e) {
           Logger.error("error while adding to cart "+e.getMessage());
           throw new Exception("Out of Stock");
       }
    }

    //TEMP TODO manjeet passing userid
    public static String getCartCount(String userId) {
        //calculate total qty and amount for current user's activce cart
        int qty=0;
        float amount=0.0f;

        Cart cart = Cart.findByUserId(userId);
        if(cart == null) return "";

        qty = cart.items.stream().mapToInt(i -> i.qty).sum();
        //TODO : calculate price based on sales, offers.
        amount = (float) cart.items.stream().mapToInt(i -> (int)(i.qty * i.itemDetail.price.mrp) ).sum();
        return "{\"qty\": "+qty+", \"amount\": "+amount+"}";
    }

    public static String getCartDetail(String userId) {
        //calculate total qty and amount for current user's activce cart
        Cart cart = Cart.findByUserId(userId);
        return stringify( toJson(cart));
    }

    public static String search(String key) {
       // Logger.info("Searching for key="+key);
        List<Product> products = new ArrayList<Product>();
        Query<Product> query = MorphiaObject.datastore.find(Product.class);
        
        query.or(query.criteria("title").contains(key) ,
                query.criteria("detail.tags").contains(key));

       // products.addAll(MorphiaObject.datastore.find(Product.class, "detail.tags =", Pattern.compile(""+key + "") ).asList());

        products.addAll(query.asList());
       // Logger.info("search output size="+products.size());
        return stringify( toJson( products )  );
    }


    public static void checkoutCart(Checkout checkout) throws Exception {
       try {
           Logger.info("Started Checking out cart.");
           Cart cart = Cart.findByUserId(checkout.userId);
           if(cart == null) {
               Logger.error("Cart is empty");
               throw new Exception("Cart is empty");
           }
           checkout.items = cart.items;
           checkout.deliveryTracking = new DeliveryTracking();
           MorphiaObject.datastore.save(checkout);

           // delete from cart
           MorphiaObject.datastore.delete(cart);
       } catch(Exception e) {
            Logger.error("error while checking out cart "+e.getMessage());
            throw new Exception("Cart is empty");
       }

        try {
            //TODO TEMP SHOULD BE DONE IN SEPERATE THREAD/ACTOR
        //send mail
            MailSender.SendMail(
                new MailSenderParam(checkout.email,"Order tracking id "+checkout.deliveryTracking.trackingNumber ,
                        "Thanks for the order. We will get back to you soon."));
        } catch(Exception e) {
            Logger.error("error while checking out cart "+e.getMessage());
            //throw new Exception("Cart is empty");
        }


    }
}
