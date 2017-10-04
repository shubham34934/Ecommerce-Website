package services;

import com.fasterxml.jackson.databind.JsonNode;
import helper.datasources.MorphiaObject;
import models.Product;
import org.mongodb.morphia.query.Query;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static play.libs.Json.stringify;
import static play.libs.Json.toJson;

public class SearchService {

    public static String search(String key) {
       // Logger.info("Searching for key="+key);
        List<Product> products = new ArrayList<Product>();
        Query<Product> query = MorphiaObject.datastore.find(Product.class);

        String[] keys = key.split("\\+| ");

        for(int i=0; i < keys.length; i++) {
            query.or(query.criteria("title"). containsIgnoreCase(keys[i]),
                    query.criteria("detail.tags").containsIgnoreCase(keys[i]));
        }
       // products.addAll(MorphiaObject.datastore.find(Product.class, "detail.tags =", Pattern.compile(""+key + "") ).asList());

        products.addAll(query.asList());
       // Logger.info("search output size="+products.size());
        return stringify( toJson( products )  );
    }

}
