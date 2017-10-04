package controllers.shop.admin;

import helper.datasources.MorphiaObject;
import org.bson.types.ObjectId;
import play.Logger;
import play.data.DynamicForm;
import services.ResourceUtil;
import play.mvc.Http.MultipartFormData.*;
import play.mvc.*;
import models.*;
import java.io.File;
import java.util.List;

import sun.security.util.ObjectIdentifier;
import views.html.shop.*;
import play.data.Form;

public class ShopAdmin extends Controller {

    public Result index() {
        return ok(admin.render("admin"));
    }

    public Result addProduct() {
        return ok(addshopitem.render(Product._FORM));
    }

    public Result doAddProduct() {
       // final Products model = new Products();
        try {
            final DynamicForm dynamicForm = Form.form().bindFromRequest();
            final String cat = dynamicForm.get("mycategory");

            final Form<Product> filledForm = Product._FORM.bindFromRequest();
            final Product model = filledForm.get();
            Logger.info("Inserting cat=" + cat);
            model.detail.tags = ResourceUtil.getParsedList(model.detail.tags.get(0).toString());

            ObjectId id = Category.getId(cat);
            if(id != null)
                model.categories.add(id);
            // model.title = fmodel.title;
            // model.detail.description = fmodel.detail.description;
            // model.price.mrp = fmodel.price.mrp;
            model.sku  = Product.GenerateSKU();
            Logger.info("Generated sku"+model.sku);


            // TODO : image
            //data send as raw data without multipart-encoding
            //File file = request().body().asRaw().asFile();
            //file.renameTo(new File("/tmp", model.image));
            Http.MultipartFormData body = request().body().asMultipartFormData();

//            FilePart uploadFilePart = body.getFile("image");
//            if (uploadFilePart != null) {
//                String fileName = uploadFilePart.getFilename();
//                String contentType = uploadFilePart.getContentType();
//                File file = uploadFilePart.getFile();
//                model.image = ResourceUtil.SaveImageFile(file, fileName);
//            }

            List<FilePart> fs =  body.getFiles();
            //boolean isFirst = true;
            int imageIndex = 0;
            for (FilePart f : fs) {
                String fileName = f.getFilename();
                String contentType = f.getContentType();
                File file = f.getFile();
                String imageName = ResourceUtil.SaveImageFile(file,
                        ResourceUtil.GenImageFileName(model.sku, imageIndex,fileName));
                if(imageIndex==0) {
                    model.image = imageName;
                    //isFirst = false;
                }
                imageIndex++;
                model.detail.images.add(imageName);
                Logger.info("Generated image Name=" + imageName);
            }

            //model.save();
            MorphiaObject.datastore.save(model);
        } catch (Exception e) {

        }
        return addProduct();//ok(index.render(null));

    }


    public Result addCategory() {
        return ok(addcategory.render(Category._FORM));
    }

    public Result doAddCategory() {
        try {
            final Form<Category> filledForm = Category._FORM.bindFromRequest();
            final Category model = filledForm.get();

            // TODO : image
            //data send as raw data without multipart-encoding
            //File file = request().body().asRaw().asFile();
            //file.renameTo(new File("/tmp", model.image));
     /*       Http.MultipartFormData body = request().body().asMultipartFormData();
            FilePart uploadFilePart = body.getFile("image");
            if (uploadFilePart != null) {
                String fileName = uploadFilePart.getFilename();
                String contentType = uploadFilePart.getContentType();
                File file = uploadFilePart.getFile();
                model.image = ResourceUtil.SaveImageFile(file, fileName);
            }
     */
            MorphiaObject.datastore.save(model);
        } catch (Exception e) {

        }
        return addCategory();
    }

}
