package models;

/**
 * Created by manjeet on 12/09/15.
 */


import org.bson.types.ObjectId;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by manjeet on 10/08/15.
 */


public class ProductDetail {
        public String description;
        public List<String> images = new ArrayList<String>();
        public List<String> tags = new ArrayList<String>();
        public List<String> features = new ArrayList<String>();
        public List<String> requirements = new ArrayList<String>();
}
