package services;

/**
 * Created by manjeet on 12/09/15.
 */


import org.imgscalr.Scalr;
import play.Logger;
import plugins.S3Plugin;
import settings.Config;
import utils.MyMath.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static play.data.Form.form;

public class ResourceUtil {


        public static String ResourceUrl() {
            if(Config.Debug)
                return "/externalAssets";
            else
                return S3Plugin.PublicURL;
        }

//        public static String getImageUrl(String imageName)  {
//                // if(File.)
//                // return "/externalAssets/cabin.png";
//                return "/externalAssets/"+imageName;
//                //return controllers.ExternalAssets.at("/tmp",title).toString();
//                // return new URL("file:/"+"//tmp/"+image);
//                //return new URL(S3Plugin.PublicURL + name);
//        }

//    public URL getImageUrl() throws MalformedURLException {
//        // if(File.)
//        return new URL(controllers.ExternalAssets.at("/tmp",title).toString());
//       // return new URL("file:/"+"//tmp/"+image);
//        //return new URL(S3Plugin.PublicURL + name);
//    }

    public static String GenImageFileName(String sku, int index, String fileName) {
        return sku+"_"+index+"_"+StringUtil.clearSpecialChar(fileName);
    }

    public static String SaveImageFile(File file, String fileName) {
        //CreateThumbnail(file,fileName,256);
        return WriteImageFile(file, fileName);
    }

    public static void CreateThumbnail(File file, String fileName, int dim) {
        String thumbFileName = "t_"+dim+"_"+fileName;
        try {
            File fileThumbnail = createThumbnail(file, dim, dim);
            WriteImageFile(fileThumbnail, thumbFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String WriteImageFile(File file, String fileName) {
        if(file==null || fileName == null) return null;

        String image = fileName;
                Logger.debug("Image url "+image);
            //TODO : EITHER SAVE IT LOCALLY OR PUSH IT TO CDN
            //PUSH THIS TO CDN
        if (S3Plugin.amazonS3 != null) {
            if(Config.Debug)
                file.renameTo(new File("/tmp",image));
            else
                S3Plugin.UploadToS3(S3Plugin.awsDir+"/"+image, file);
        } else {
            Logger.error("Could not save because amazonS3 was null. So saving locally");
            //save locally
            file.renameTo(new File("/tmp",image));
        }
           return image;
    }

//http://paxcel.net/blog/java-thumbnail-generator-imagescalar-vs-imagemagic/
    public static File createThumbnail(File sourceImage, int width,int height) throws IOException {
        boolean needThumbnail = true;
        String destImageName = "temp"+"_"+width+"x"+height+"_"+ "thunbnail.jpg";
        File destImage = new File(destImageName);
        if (needThumbnail) {
            BufferedImage img = ImageIO.read(sourceImage);
            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, width, height, Scalr.OP_ANTIALIAS);
            ImageIO.write(thumbImg,"jpg", destImage);
            return destImage;
        }
        return null;
    }

    public static String DELIMITER = ",";
    public static List<String> getParsedList(String value) {
        String[] tagsarray = value.split(DELIMITER);
        List<String> l = new ArrayList<String>();
        for(int i=0 ; i < tagsarray.length ; i++ ) {
            l.add(tagsarray[i]);
        }
        return l;
    }
}
