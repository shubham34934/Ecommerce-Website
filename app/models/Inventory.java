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
public class Inventory {

        @Id
        public ObjectId _id;
        public String sku; // products foriegn id
        public int quantity = 0;
        @Embedded
        public List<CartedProduct> carted = new ArrayList<CartedProduct>();

        public static final Form<Inventory> _FORM = form(Inventory.class);

        public static Query<Inventory> finder =  MorphiaObject.datastore.createQuery(Inventory.class);

        public static Inventory findById(String id) {
                try {
                        return MorphiaObject.datastore.get(Inventory.class, new ObjectId(id));
                } catch (Exception e) {
                        return null;
                }
        }

}
