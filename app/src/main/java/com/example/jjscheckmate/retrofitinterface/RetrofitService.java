package com.example.jjscheckmate.retrofitinterface;

import com.example.jjscheckmate.UploadedSurveyDTO;
import com.example.jjscheckmate.community.model.ChatRoomTempDTO;
import com.example.jjscheckmate.community.model.CommentDTO;
import com.example.jjscheckmate.community.model.CommentReplyDTO;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.community.model.GroupDTO;
import com.example.jjscheckmate.community.model.PostDTO;
import com.example.jjscheckmate.community.model.PostImageDTO;
import com.example.jjscheckmate.mainActivityViwePager.SurveyDTO;
import com.example.jjscheckmate.messageservice.MessageDTO;
import com.example.jjscheckmate.offlineform.FormItem;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RetrofitService {

    /*Survey*/
    @GET("form/{type}/{pages}")
    Call<ArrayList<SurveyDTO>> getSurveyList(@Path("type") String type, @Path("pages")Integer page);

    @GET("form/{userEmail}")
    Call<ArrayList<UploadedSurveyDTO>> getSurveyList(@Path("userEmail") String userEmail);

    @GET("Draftform/{userEmail}")
    Call<ArrayList<FormItem>> getDraftSurveyList(@Path("userEmail") String userEmail);

    @GET("form/{type}/{pages}")
    Call<ArrayList<SurveyDTO>> getAllSurveyList(@Path("type") String type, @Path("pages")Integer page);

    @GET("search/{queryText}/{pages}")
    Call<ArrayList<FriendDTO>> getSearchResult(@Path("queryText") String queryText, @Path("pages")Integer page);

    /*Friend*/
    @FormUrlEncoded
    @POST("friend/request")
    Call<Boolean> friendRequest(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/select")
    Call<ArrayList<FriendDTO>> friendSelect(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/update")
    Call<Boolean> friendUpdate(@FieldMap HashMap<String, Object> param);

    @GET("user/{userEmail}/{state}")
    Call<ArrayList<FriendDTO>> getFriendList(@Path("userEmail") String userEmail,@Path("state") Integer state);

    /*Group*/
    @FormUrlEncoded
    @POST("group/join")
    Call<Boolean> groupJoin(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("group/passwordCheck")
    Call<RetrofitResponse> groupPasswordCheck(@FieldMap HashMap<String, Object> param);

    @GET("group/all/{userEmail}/{count}/{offset}")
    Call<ArrayList<GroupDTO>> grouptGet(@Path("userEmail")String userEmail, @Path("count")Integer count, @Path("offset")Integer offset);

    @Multipart
    @POST("group/create")
    Call<Boolean> groupCreate(@PartMap HashMap<String, Object> param, @Part MultipartBody.Part file);

    @GET("group/my/{userEmail}")
    Call<ArrayList<GroupDTO>> getMyGroup(@Path("userEmail")String userEmail);

    //그룹 탈퇴
    @DELETE("group/withdraw/{id}")
    Call<RetrofitResponse> groupWithdraw(@Path("id")Integer id);

    /* 워드 클라우드 */
    @GET("search_keyword/{keyword}")
    Call<ArrayList<SurveyDTO>> getKeywordSurveyList(@Path("keyword") String keyword);

    /* 채팅 */
    @GET("chat/rooms/{userEmail}")
    Call<ArrayList<ChatRoomTempDTO>> getRoomList(@Path("userEmail") String userEmail);

    //로컬 캐시와 디비의 메세지 캐시가 차이 날경우 디비의 메세지를 긁어 온다
    @GET("chat/rooms/{roomKey}/{count}/{offset}")
    Call<ArrayList<MessageDTO>> getRoomMessages(@Path("roomKey") String roomKey
            , @Path("count") Integer count
            , @Path("offset") Integer offset);

    //채팅방,채팅 관련 요청
    @FormUrlEncoded
    @POST("chat/create")
    Call<Boolean> chatRoomCreateRequest(@FieldMap HashMap<String, Object> param);

    /* 게시물 */
    @DELETE("post/delete/{id}")
    Call<RetrofitResponse> postDelete(@Path("id")Integer id);

    @GET("post/{groupID}/{count}/{offset}")
    Call<ArrayList<PostDTO>> getPost(@Path("groupID")Integer groupID, @Path("count")Integer count, @Path("offset")Integer offset);

    @Multipart
    @POST("post/create")
    Call<PostDTO> postCreate(@PartMap HashMap<String, Object> param,@Part ArrayList<MultipartBody.Part> files);

    @GET("post/images/{postID}")
    Call<ArrayList<PostImageDTO>> getImageLen (@Path("postID")Integer postID);

    @Multipart
    @PUT("post/update")
    Call<PostDTO> postUpdate(@PartMap HashMap<String, Object> param,@Part ArrayList<MultipartBody.Part> files);

    /* 댓글 */
    @FormUrlEncoded
    @POST("post/add_comment")
    Call<Boolean> commentCreate(@FieldMap HashMap<String, Object> param);

    @GET("post/delete_comment/{id}")
    Call<Boolean> deleteComment(@Path("id")Integer _id);

    @GET("post/comment/{postID}/{count}/{offset}")
    Call<ArrayList<CommentDTO>> getComment(@Path("postID")Integer groupID, @Path("count")Integer count, @Path("offset")Integer offset);

    /* 대댓글 */
    @GET("post/comment_reply/{commentID}/{count}/{offset}")
    Call<ArrayList<CommentReplyDTO>> getReply(@Path("commentID")Integer groupID, @Path("count")Integer count, @Path("offset")Integer offset);

    @FormUrlEncoded
    @POST("post/add_comment_reply")
    Call<Boolean> replyCreate(@FieldMap HashMap<String, Object> param);

    @GET("post/delete_reply/{id}")
    Call<Boolean> deleteReply(@Path("id")Integer _id);

    /* 프로필 */
    @Multipart
    @POST("user/profile/upload")
    Call<ResponseBody> profileImageUpload(@Part("userEmail") String userEmail,@Part MultipartBody.Part file);

    /* 위젯 */
    @GET("search_id/{id}")
    Call<ArrayList<UploadedSurveyDTO>> get_idSurveyList(@Path("id") Integer id);
}
