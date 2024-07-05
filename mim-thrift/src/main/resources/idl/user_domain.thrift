namespace java com.mdc.mim.user

include "base.thrift"

struct User {
    1: optional i64 Uid,
    2: optional string UserName,
    3: optional string PasswdMd5,
    4: optional string NickName,
    5: optional string RegisterDate, // Thrift不支持Date类型，使用string代替
}

struct IdentifyReq {
    1: required string UserName,
    2: required string PasswdMd5,
}

struct IdentifyResp {
    1: required bool Valid,
    2: optional User User,
    255: optional base.BaseResp BaseResp,
}

struct FindUidReq {
    1: required string UserName,
}

struct FindUidResp {
    1: optional i64 Uid,
    255: optional base.BaseResp BaseResp,
}