package models.sql;

/**
 * Created by manjeet on 12/09/15.
 */


import java.util.*;
import javax.persistence.*;

import play.data.Form;
import static play.data.Form.form;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ShopItemModel extends Model {

        @Id
        @Constraints.Min(10)
        public Long id;

        @Constraints.Required
        public String title;

        public String image;

        @Formats.DateTime(pattern="dd/MM/yyyy")
        public Date creationDate = new Date();

        public static Finder<Long,ShopItemModel> find = new Finder<Long,ShopItemModel>(
                Long.class, ShopItemModel.class
        );


        public static final Form<ShopItemModel> _FORM = form(ShopItemModel.class);
}
