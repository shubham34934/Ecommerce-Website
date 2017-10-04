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
public class Checkout {

        @Id
        public ObjectId _id;
        //USER ID
        public String userId;
        // for non loggedin user
        public String email;
        public String phone;

        @Embedded
        public Address deliveryAddress;
        public String orderComment;
        @Embedded
        public DeliveryTracking deliveryTracking;
        @Embedded
        public List<Cart.CartItem> items = new ArrayList<Cart.CartItem>();
        /******************************/

        public static final Form<Checkout> _FORM = form(Checkout.class);

        public static Query<Checkout> finder =  MorphiaObject.datastore.createQuery(Checkout.class);


        public static Checkout findById(String id) {
                return findById( new ObjectId(id));
        }

        public static Checkout findById(ObjectId id) {
                try {
                        return MorphiaObject.datastore.get(Checkout.class, id);
                        // MorphiaObject.datastore.find(Cart.class, "_id =", cartId).get();
                } catch (Exception e) {
                        return null;
                }
        }

}
