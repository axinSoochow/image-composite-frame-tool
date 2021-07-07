# coding=utf-8
import json

data_name = "data/XAS001"
data_name2 = "data/DLS002"
data_name3 = "data/ZZS005"
data_name4 = "data/QDS001"
data_name5 = "data/HFS001-J"
data_name6 = "data/HFS001-Z"

# 新榜生成更新sql
if __name__ == '__main__':
    a = json.load(open(data_name6, encoding='utf8'))
    for case in a:
        ratio = round((case['juryRatio'] + case["ordinaryRatio"]),3)
        print("UPDATE t_case_ratio set juryCount = \'{}\', juryRatio = \'{}\' ,ordinaryCount = \'{}\',ordinaryRatio = \'{}\' ,ratio = \'{}\' where caseId = \'{}\' and activityId = 41 and phase = 1;"
              .format(case['juryCount'],case['juryRatio'],case["ordinaryCount"],case["ordinaryRatio"],ratio,case["caseId"]))
