package models;

/**
 * Created by manjeet on 12/09/15.
 */


import helper.datasources.MorphiaObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import play.data.Form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by manjeet on 10/08/15.
 */

@Entity(noClassnameStored = true)
public class Cart {

        @Id
        public ObjectId _id;
        //USER ID //temp
        public String userId;

        public CartStatus status=CartStatus.ACTIVE;
        public Date last_modified = new Date();
        @Embedded
        public List<CartItem> items = new ArrayList<CartItem>();

        /******************************/

        public static final Form<Cart> _FORM = form(Cart.class);

        public static Query<Cart> finder =  MorphiaObject.datastore.createQuery(Cart.class);

        public static Cart findByUserId(String id) {
                try {
                        return MorphiaObject.datastore.find(Cart.class, "userId =", id).get();
                } catch (Exception e) {
                        return null;
                }
        }


        public static Cart findById(String id) {
                return findById( new ObjectId(id));
        }

        public static Cart findById(ObjectId id) {
                try {
                        return MorphiaObject.datastore.get(Cart.class, id);
                        // MorphiaObject.datastore.find(Cart.class, "_id =", cartId).get();
                } catch (Exception e) {
                        return null;
                }
        }

        public static void deleteById(String id) {
                try {
                        //TODO : DELETE THE IMAGE FROM CDN/LOCAL IF NOT USED ELSE WHERE
                        MorphiaObject.datastore.delete(Product.class, new ObjectId(id));
                } catch (Exception e) {

                }
        }

        public enum CartStatus {
                ACTIVE,
                PENDING,
                EXPIRING,
                EXPIRED,
                COMPLETE
        }

        public static class CartItem {
                //public int itemIndex;
                public String sku;
                public int qty=1;
                //period
                public Date startDate;
                public Date endDate;

                //few minimal detail so that no join query required
                @Embedded
                public ItemDetail itemDetail;

                public static final Form<CartItem> _FORM = form(CartItem.class);

                public CartItem() {

                }

                public CartItem(String sku, int qty) {
                        this.sku = sku;
                        this.qty =qty;
                        repopulateItemDetail();
                }

                public void repopulateItemDetail() {
                        Cart.ItemDetail itemDetail  = new Cart.ItemDetail();
                        Product product = MorphiaObject.datastore.find(Product.class, "sku =", sku).get();
                        itemDetail.image = product.image;
                        itemDetail.title = product.title;
                        itemDetail.price = product.price;
                        this.itemDetail = itemDetail;
                }
        }

        public static class ItemDetail {
                public String title;
                public String image;
                @Embedded
                public ProductPrice price;
        }
}
