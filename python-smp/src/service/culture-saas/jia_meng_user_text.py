# coding=utf-8
import json

data_name = "data/t_case_vote.json"
result_name = "data/after_user.txt"

# 新榜生成更新sql
if __name__ == '__main__':
    a = json.load(open(data_name, encoding='utf8'))
    f = open(result_name, 'w')
    for user in a:
        f.write(str(user["cuser"]-1000000000000000)+"\n")
    f.close()





