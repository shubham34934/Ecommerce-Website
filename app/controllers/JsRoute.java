package controllers;

/**
 * Created by manjeet on 16/09/15.
 */


import controllers.shop.user.*;
import controllers.shop.user.routes;
import play.Routes;
import play.mvc.Controller;
import play.mvc.Result;

public class JsRoute extends  Controller{

    public Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        routes.javascript.ShopApi.featuredItem()
                )
        );
    }
}
