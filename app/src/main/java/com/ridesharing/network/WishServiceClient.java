package com.ridesharing.network;

import com.ridesharing.App;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultDataWishList;
import com.ridesharing.Entity.Wish;

import org.springframework.core.ParameterizedTypeReference;

/**
 * Created by wensheng on 2014/11/10.
 */
public class WishServiceClient {
    private static final String BASE_URL = App.BaseURL + "WishService.php";

    private static Result base(String type, Wish wish){
        String url = BASE_URL + "?type=" + type;
        RestHelper<Wish, Result> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<Result>() {});
        return helper.execute();
    }

    public static ResultDataWishList search(Wish wish){
        String url = BASE_URL + "?type=search";
        ResultDataWishList res = new ResultDataWishList();
        RestHelper<Wish, ResultDataWishList> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<ResultDataWishList>() {});
        return helper.execute();
    }

    public static Result add(Wish wish){
        return base("add", wish);
    }

    public static Result remove(Wish wish){
        return base("delete", wish);
    }

    public static Result update(Wish wish){return  base("update", wish);}

}
