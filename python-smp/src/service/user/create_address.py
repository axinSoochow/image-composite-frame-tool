# coding=utf-8

from src.service.user.smp_register_processer import *

data_name = "data/address/create_address.txt"

env = "http://172.16.23.4:8080"
env_local = "http://localhost:6666"
bizAccount = "1000000026029855"
tenantCode = "jiayou"
accountType = "uc"
id = 0
ucid = 0

nums = 1
# 电商中台 - 用户收货地址新增脚本
if __name__ == '__main__':
    param = json.loads(open(data_name).read())
    param["bizAccount"] = bizAccount
    param["tenantCode"] = tenantCode
    param["accountType"] = accountType
    if id != 0:
        param["address"]["id"] = id
    if ucid != 0:
        param["address"]["ucid"] = ucid
    processer = SmpUserProcesser(env_local)
    try:
        api_result = processer.create_address(param)
        print(api_result.text)
    except BaseException as err:
        print("服务未调用成功，请检查url:{}是否正确!，错误原因:{}".format(env_local, err))
        quit()

    for i in range(1, nums):
        api_result = processer.create_address(param)
        print(api_result.text)
