package models;

/**
 * Created by manjeet on 12/09/15.
 */


import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by manjeet on 10/08/15.
 */


public class CartedProduct {
        public ObjectId cartId;
        public int quantity;
        public Date timestamp = new Date();
}
