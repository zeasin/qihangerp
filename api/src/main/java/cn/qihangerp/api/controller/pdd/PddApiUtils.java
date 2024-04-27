package cn.qihangerp.api.controller.pdd;


import cn.qihangerp.common.ApiResult;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddMallInfoGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddMallInfoGetResponse;
import cn.qihangerp.common.ResultVoEnum;


public class PddApiUtils {
    /**
     * 获取店铺信息（原则上所有api调用之前都需要调用一下该方法）
     * @param clientId
     * @param clientSecret
     * @param accessToken
     * @return
     * @throws Exception
     */
    public static ApiResult<PddMallInfoGetResponse.MallInfoGetResponse> getShopInfo(String clientId, String clientSecret, String accessToken) throws Exception {

        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddMallInfoGetRequest request = new PddMallInfoGetRequest();
        PddMallInfoGetResponse response = client.syncInvoke(request, accessToken);
        if (response.getErrorResponse() == null) {
            return new ApiResult<>(ResultVoEnum.SUCCESS.getIndex(), "SUCCESS", response.getMallInfoGetResponse());
        } else if (response.getErrorResponse().getErrorCode().intValue() == 10019) {
            return new ApiResult<>(ResultVoEnum.TokenFail.getIndex(), "Token过期");
        }else
            return new ApiResult<>(ResultVoEnum.Fail.getIndex(), "接口调用失败："+response.getErrorResponse().getErrorMsg());

    }
}
