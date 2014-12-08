package com.ridesharing.network;

import com.ridesharing.App;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultData;
import com.ridesharing.Entity.Wish;

import org.androidannotations.annotations.rest.Rest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;

/**
 * Created by wensheng on 2014/11/10.
 */
public class WishServiceClient {
    private static final String BASE_URL = App.BaseURL + "WishService.php";

    private static Result base(String type, Wish wish){
        String url = BASE_URL + "?type=" + type;
        RestHelper<Wish, Result> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<Result>() {});
        return helper.execute(false);
    }

    public static ResultData<Wish> getSpecificWish(int wishId){
        String url = BASE_URL + "?type=getWish&wishId=" + wishId;
        RestHelper<String, ResultData<Wish>> helper = new RestHelper<>(url, "", new ParameterizedTypeReference<ResultData<Wish>>() {});
        return helper.execute(true);
    }

    public static ResultData<ArrayList<Wish>> search(Wish wish){
        String url = BASE_URL + "?type=search";
        ResultData<ArrayList<Wish>> res = new ResultData<ArrayList<Wish>>();
        RestHelper<Wish, ResultData<ArrayList<Wish>>> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<ResultData<ArrayList<Wish>>>() {});
        return helper.execute(true);
    }

    public static ResultData<ArrayList<Wish>> searchAndAdd(Wish wish){
        String url = BASE_URL + "?type=searchAndAdd";
        ResultData<ArrayList<Wish>> res = new ResultData<ArrayList<Wish>>();
        RestHelper<Wish, ResultData<ArrayList<Wish>>> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<ResultData<ArrayList<Wish>>>() {});
        return helper.execute(true);
    }

    public static ResultData<ArrayList<Wish>> fetchAll(Wish wish, int distance){
        String url = BASE_URL + "?type=all&distance=" + distance;
        ResultData<ArrayList<Wish>> res = new ResultData<ArrayList<Wish>>();
        RestHelper<Wish, ResultData<ArrayList<Wish>>> helper = new RestHelper<>(url, wish, new ParameterizedTypeReference<ResultData<ArrayList<Wish>>>() {});
        return helper.execute(true);
    }

    public static Result add(Wish wish){
        return base("add", wish);
    }

    public static Result remove(Wish wish){
        return base("delete", wish);
    }

    public static Result update(Wish wish){return  base("update", wish);}

}
