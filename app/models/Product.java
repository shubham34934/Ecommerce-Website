package models;

/**
 * Created by manjeet on 12/09/15.
 */


import org.mongodb.morphia.annotations.Id;
import play.data.Form;

import static play.data.Form.form;
import helper.datasources.MorphiaObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mongodb.morphia.annotations.Entity;

import org.mongodb.morphia.annotations.Embedded;
/**
 * Created by manjeet on 10/08/15.
 */



@Entity(noClassnameStored = true)
public class Product {
        @Id
        public ObjectId _id;
        public String sku;
        public String title;

        //TODO : Change it to @transient which will have resurl,mimetype,storagetypeENUM
        public String image;
        @Embedded
        public ProductPrice price;
        @Embedded
        public ProductDetail detail;

        public int qty;
        //@Embedded
        //public List<CartedProduct> carted = new ArrayList<CartedProduct>();

        public List<ObjectId> categories = new ArrayList<ObjectId>();
        // public Date creationDate = new Date();


/******************************************************************************/




        public static final Form<Product> _FORM = form(Product.class);

        public static Query<Product> finder =  MorphiaObject.datastore.createQuery(Product.class);


        public static Product findById(String id) {
                try {
                        return MorphiaObject.datastore.get(Product.class, new ObjectId(id));
                } catch (Exception e) {
                        return null;
                }
        }

        public static Product findBySku(String sku) {
                try {
                        return MorphiaObject.datastore.find(Product.class, "sku =", sku).get();
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

        //takes user name as parameter...etc
        public static String GenerateSKU() {
                //sku = _id.toHexString();
                //new ObjectId().toHexString();
                return UUID.randomUUID().toString();

//                $vowels = array('a', 'e', 'i', 'o', 'u', 'y'); // vowels
//                preg_match_all('/[A-Z][a-z]*/', ucfirst($string), $m); // Match every word that begins with a capital letter, added ucfirst() in case there is no uppercase letter
//                foreach($m[0] as $substring){
//                        $substring = str_replace($vowels, '', strtolower($substring)); // String to lower case and remove all vowels
//                        $results .= preg_replace('/([a-z]{'.$l.'})(.*)/', '$1', $substring); // Extract the first N letters.
//                }
//                $results .= '-'. str_pad($id, 4, 0, STR_PAD_LEFT); // Add the ID
                //it should depend on userid and added item number
                //Logger.info("Generated sku"+sku);
        }


        //TODO: brief json reply for all product query;
        public static class ProductBrief {
                public String sku;
                public String title;
                public String image;
                public String desc;
                public ProductPrice price;

                public ProductBrief(String sku, String title, String image, String desc, ProductPrice price) {
                        this.sku = sku;
                        this.title = title;
                        this.image = image;
                        this.desc = desc;
                        this.price = price;
                }
                public ProductBrief(Product product) {
                        this.sku = product.sku;
                        this.title = product.title;
                        this.image = product.image;
                        this.desc = product.detail.description;
                        this.price = product.price;
                }
        }
}
