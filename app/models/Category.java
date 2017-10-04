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
import java.util.List;
import java.util.UUID;

import static play.data.Form.form;
/**
 * Created by manjeet on 10/08/15.
 */

@Entity(noClassnameStored = true)
public class Category {

        @Id
        public ObjectId _id;
        public String name;
        public String slug;
        public String path;
        public String image;


        public List<ObjectId> categories = new ArrayList<ObjectId>();


        public static final Form<Category> _FORM = form(Category.class);

        public static Query<Category> finder =  MorphiaObject.datastore.createQuery(Category.class);

        public static List<Category> getDescendants(String node) {
                try {
                        return MorphiaObject.datastore.find(Category.class, "path =", node ).asList();
                } catch (Exception e) {
                        return null;
                }
        }

        public static Category findById(String id) {
                try {
                        return MorphiaObject.datastore.get(Category.class, new ObjectId(id));
                } catch (Exception e) {
                        return null;
                }
        }

        public static ObjectId getId(String slug) {
                try {
                        Category cat = MorphiaObject.datastore.find(Category.class, "slug =", slug ).get();
                        if(cat !=null)
                                return cat._id;
                } catch (Exception e) {
                        return null;
                }
                return null;
        }

}
