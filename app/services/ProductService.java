package services;

import com.fasterxml.jackson.databind.JsonNode;
import helper.datasources.MorphiaObject;
import models.Category;
import models.Product;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import play.Logger;

import static play.libs.Json.*;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    public static JsonNode featuredItem() {
        List<Product.ProductBrief> productBriefs;
        //products = Product.finder.asList().stream().filter(i -> i.title.contains("bag")).collect(Collectors.toList());
        productBriefs = Product.finder.asList()
                .stream()
                .map(j -> new Product.ProductBrief(j.sku, j.title, j.image, j.detail.description, j.price))
                .collect(Collectors.toList());
        return toJson(productBriefs);
    }

    public static String getProducts(String category) {
        Logger.info("Get product for cat="+category);
        //List<Product> products;
        List<Product.ProductBrief> productBriefs;

        if( category.equalsIgnoreCase("misc") ) {

            Logger.info("Get product if misc for cat="+category);
//            productBriefs = Product.finder.field("categories").doesNotExist().asList()
//                    .stream()
//                    .map(j -> new Product.ProductBrief(j.sku, j.title, j.image, j.detail.description, j.price))
//                    .collect(Collectors.toList());

          productBriefs = Product.finder.asList().stream().filter(i -> i.categories == null || i.categories.isEmpty())
                    .map(j -> new Product.ProductBrief(j.sku, j.title, j.image, j.detail.description, j.price))
                    .collect(Collectors.toList());
        } else {
            Logger.info("Get product if not misc for cat="+category);
            ObjectId catid = Category.getId(category);
            // products = Product.finder.field("categories").hasThisOne(catid).asList();
//            productBriefs = Product.finder.filter("categories", catid).asList()
//                    .stream()
//                    .map(j -> new Product.ProductBrief(j.sku, j.title, j.image, j.detail.description, j.price))
//                    .collect(Collectors.toList());
            productBriefs = Product.finder.asList().stream().filter(i -> i.categories.contains(catid))
                    .map(j -> new Product.ProductBrief(j.sku,j.title,j.image,j.detail.description,j.price))
                    .collect(Collectors.toList());
        }
        return stringify( toJson(productBriefs) );
    }

    public  static String getProduct(String sku) {
        Product p = Product.findBySku(sku);
        return stringify( toJson( p )   );
    }

    public static String getCategoryTree() {
        return stringify( toJson(Category.finder.asList()) );
    }

    public static String getCategoryLeaf() {
        return stringify( toJson(Category.finder.asList()) );
    }
}
