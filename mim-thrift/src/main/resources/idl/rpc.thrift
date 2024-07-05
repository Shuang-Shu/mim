// 用户相关的rpc接口
namespace java com.mdc.mim.user

include "user_domain.thrift"

service UnifyService {
    user_domain.IdentifyResp Identify(1: user_domain.IdentifyReq req),
    user_domain.FindUidResp FindUidByUserName(1: user_domain.FindUidReq req),
}