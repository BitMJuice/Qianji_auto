/*
 * Copyright (C) 2021 dreamn(dream@dreamn.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.dreamn.qianji_auto.core.db.Table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AutoBill {
    //账户列表
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String type;//账单类型
    public String money;//账户名
    public String time;//账户名
    public String remark;//备注信息
    public String catename;//分类
    public String catechoose;//账户名
    public String bookname;//账本名称
    public String accountname;//账单所属资产名称
    public String accountname2;//转账或者还款的转入账户
    public String shopAccount;//识别出来的收款账户
    public String shopRemark;//识别出来的备注
    public String billInfo;//识别出来的账单数据集
    public String source;//来源
}
