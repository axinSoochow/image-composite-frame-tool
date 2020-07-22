# coding=utf-8
import json
import time;

# 链接中台fmp系统
import requests


# 动态生成参数中的唯一变量
def change_key(key_list, param):
    for key in key_list:
        for user in param["registerList"]:
            new_key = "axin_{}".format(int(round(time.time() * 1000)))
            time.sleep(1)
            del user[key]
            user[key] = new_key


# 电商中台用户域处理器
class SmpUserProcesser(object):
    # 接口地址
    register_path = "/api/v1/user/register"
    query_account_path = "/api/v1/user/account"
    query_account_batch_path = "/api/v1/user/batch/account"
    query_account_info_path = "/api/v1/user/info"
    update_user_path = "/api/v1/user/update"
    tenant_path = "/api/v1/user/tenant"

    create_address_path = "/api/v1/user/address/save"

    def __init__(self, env):
        self.env = env

    # 注册接口
    def register_user(self, param):
        return requests.post(self.env + self.register_path, data=json.dumps(param),
                             headers={"Content-Type": "application/json;charset=UTF-8"})

    # 查询映射关系接口
    def query_account_info(self, ucid, smp_id, biz_account, tenant_code, account_type):
        get_param = {"ucid": ucid, "smpId": smp_id, "bizAccount": biz_account, "tenantCode": tenant_code,
                     "accountType": account_type}
        return requests.get(self.env + self.query_account_path, get_param)

    # 批量查询关系接口
    def batch_account_info(self, ucids, smp_ids, tenant_code, biz_accounts, account_type):
        get_param = {"ucids": ucids, "smpIds": smp_ids, "tenantCode": tenant_code, "bizAccounts": biz_accounts,
                     "accountType": account_type}
        return requests.get(self.env + self.query_account_batch_path, get_param)

    # 用户基本信息查询
    def query_user_info(self, ucid, account_type, biz_account, tenant_code):
        get_param = {"ucid": ucid, "accountType": account_type, "bizAccount": biz_account, "tenantCode": tenant_code}
        return requests.get(self.env + self.query_account_info_path, get_param)

    # 用户信息更新接口
    def update_user_info(self, param):
        return requests.post(self.env + self.update_user_path, data=json.dumps(param),
                             headers={"Content-Type": "application/json;charset=UTF-8"})

    # 查询租户信息接口
    def query_tenant_info(self, tenant_id, tenant_code):
        get_param = {"tenantId": tenant_id, "tenantCode": tenant_code}
        return requests.get(self.env + self.tenant_path, get_param)

    # —————————————————— 用户收货地址 ——————————————————

    # 创建收货地址
    def create_address(self, param):
        return requests.post(self.env + self.create_address_path, data=json.dumps(param),
                             headers={"Content-Type": "application/json;charset=UTF-8"})
