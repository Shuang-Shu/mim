namespace java com.mdc.mim.base

struct BaseResp {
    1: required string StatusMessage = "",
    2: required i32 StatusCode = 0,
    3: optional map<string, string> Extra,
}