package com.cxwl.ichangxing.apis;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Mersens on 2016/9/28.
 */

public interface ServiceStore {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/auth/login")
    Observable<ResponseBody> login(@Body RequestBody RequestBody);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/auth/register")
    Observable<ResponseBody> register(@Body RequestBody RequestBody);

    @FormUrlEncoded
    @POST("app/auth/getMobileCode")
    Observable<ResponseBody> getMobileCode(@Field("mobile") String mobile);

    @POST("app/auth/logout")
    Observable<ResponseBody> logout();


    @POST("app/user/info")
    Observable<ResponseBody> getUserInfo(@Header ("token") String token);

    @POST("app/dedicatedLine/list")
    Observable<ResponseBody> getOftenLineInfo(@Header ("token") String token);

    @POST("app/dedicatedLine/delete")
    Observable<ResponseBody> oftenLineDel(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/dedicatedLine/save")
    Observable<ResponseBody> oftenLineSave(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/user/updatePassword")
    Observable<ResponseBody> updatePassword(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/grab/getGrabList")
    Observable<ResponseBody> getGrabList(@Header ("token") String token,@Query("pageNo") String pageNo,
                                         @Query("pageSize") String pageSize,@Body RequestBody RequestBody);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/grab/getGrabList")
    Observable<ResponseBody> getGrabInfo(@Header ("token") String token,@Query("pageNo") String pageNo,
                                         @Query("pageSize") String pageSize,@Body RequestBody RequestBody);

    @POST("app/pallet/findAcceptOrder")
    Observable<ResponseBody> findAccept(@Header ("token") String token,@Query("pageNo") String pageNo,
                                         @Query("pageSize") String pageSize,@Query("orderBy") String orderBy,@Body RequestBody RequestBody);

    @POST("app/appNotice/notice/find")
    Observable<ResponseBody> findNotice(@Header ("token") String token,@Query("pageNo") String pageNo,
                                        @Query("pageSize") String pageSize);

    @POST("app/appNotice/notice/noticeCount")
    Observable<ResponseBody> noticeCount(@Header ("token") String token);

    @POST("app/appNotice/notice/deleted")
    Observable<ResponseBody> deleted(@Header ("token") String token,@Query("noticeId") String noticeId);

    @POST("app/appNotice/notice/save")
    Observable<ResponseBody> saveNotice(@Header ("token") String token,@Query("noticeId") String noticeId,
                                        @Query("readStatus") Integer readStatus);

    @POST("app/grab/grabPallet")
    Observable<ResponseBody> grabPallet(@Header ("token") String token,@Query("palletId") String palletId,
                                        @Query("formId") String formId,@Query("lx") String lx,
                                        @Query("ly") String ly);
    @POST("app/grab/cancelGrab")
    Observable<ResponseBody> cancelGrab(@Header ("token") String token,@Query("acceptId") String acceptId);

    @POST("app/pallet/refuse")
    Observable<ResponseBody> refuse(@Header ("token") String token,@Query("acceptId") String acceptId,
                                    @Query("remark") String remark);

    @POST("app/pallet/accept")
    Observable<ResponseBody> accept(@Header ("token") String token,@Query("acceptId") String acceptId,
                                        @Query("lx") String lx,
                                        @Query("ly") String ly);

    @POST("app/pallet/drive")
    Observable<ResponseBody> drive(@Header ("token") String token,@Query("acceptId") String acceptId );

    @POST("app/pallet/detail")
    Observable<ResponseBody> orderDetail(@Header ("token") String token,@Query("acceptId") String acceptId );

    @Multipart
    @POST("app/picture/uploadCargo")
    Observable<ResponseBody> uploadCargo(@Header ("token") String token,@Part MultipartBody.Part file );

    @Multipart
    @POST("app/picture/upload")
    Observable<ResponseBody> upload(@Header ("token") String token,@Part MultipartBody.Part file );

    @POST("app/land/track/load")
    Observable<ResponseBody> trackLoad(@Header ("token") String token,@Body RequestBody RequestBody);


    @POST("app/location/upload")
    Observable<ResponseBody> locationUnload(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/land/track/unload")
    Observable<ResponseBody> trackUnLoad(@Header ("token") String token,@Body RequestBody RequestBody);


    @POST("app/land/track/exception")
    Observable<ResponseBody> trackException(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST(" app/land/track/getLoad")
    Observable<ResponseBody> getLoad(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/land/track/getException")
    Observable<ResponseBody> getException(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/land/track/getUnload")
    Observable<ResponseBody> getUnload(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/land/track/getReceipt")
    Observable<ResponseBody> getReceipt(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/pallet/finish")
    Observable<ResponseBody> orderFinish(@Header ("token") String token,@Query("acceptId") String acceptId );

    @POST("app/order/driveList")
    Observable<ResponseBody> driveList(@Header ("token") String token,@Query("pageNo") String pageNo,
                                        @Query("pageSize") String pageSize,@Query("terms") String terms,@Body RequestBody RequestBody);

    @POST("app/order/receipt")
    Observable<ResponseBody> receipt(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/land/track/receipt")
    Observable<ResponseBody> trackReceipt(@Header ("token") String token,@Body RequestBody RequestBody);
    @POST("app/basic/dict")
    Observable<ResponseBody> dict(@Header ("token") String token,@Query("type") String type );

    @POST("app/order/incomeSt")
    Observable<ResponseBody> incomeSt(@Header ("token") String token);

    @POST("app/user/applyWithdraw")
    Observable<ResponseBody> applyWithdraw(@Header ("token") String token,@Query("amount") String amount);

    @POST("app/user/withdrawDetail")
    Observable<ResponseBody> withdrawDetail(@Header ("token") String token,@Query("pageNo") String pageNo,
                                       @Query("pageSize") String pageSize,@Query("terms") String terms);

    @POST("app/user/getPayRecord")
    Observable<ResponseBody> getPayRecord(@Header ("token") String token,@Query("pageNo") String pageNo,
                                            @Query("pageSize") String pageSize);

    @POST("app/user/getBaseInfo")
    Observable<ResponseBody> getBaseInfo(@Header ("token") String token);

    @POST("app/user/setBaseInfo")
    Observable<ResponseBody> setBaseInfo(@Header ("token") String token,@Body RequestBody RequestBody);

    @POST("app/user/bindBank")
    Observable<ResponseBody> bindBank(@Header ("token") String token,@Query("bank") String bank,
                                            @Query("bankAccount") String bankAccount,@Query("accountName") String accountName);

    @POST("app/basic/place")
    Observable<ResponseBody> place(@Header ("token") String token,@Query("parentId") String parentId );

}
