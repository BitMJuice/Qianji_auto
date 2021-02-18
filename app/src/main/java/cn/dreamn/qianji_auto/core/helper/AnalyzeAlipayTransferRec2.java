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

package cn.dreamn.qianji_auto.core.helper;

import android.content.Context;

import java.util.List;

import cn.dreamn.qianji_auto.core.base.alipay.Alipay;
import cn.dreamn.qianji_auto.core.utils.CallAutoActivity;
import cn.dreamn.qianji_auto.core.utils.BillInfo;
import cn.dreamn.qianji_auto.core.utils.BillTools;
import cn.dreamn.qianji_auto.core.db.Helper.Caches;
import cn.dreamn.qianji_auto.utils.tools.Logs;

class AnalyzeAlipayTransferRec2 {
    private final static String TAG = "alipay_transfer2";
    //获取备注


    public static boolean succeed(List<String> list, Context context) {

        //[Dreamn在线收款, +1.00, 交易成功, 转账说明, 劳务费, 创建时间, 2021-02-14 15:19, 更多, 账单分类, 转账红包, 标签和备注, 添加, 查看往来记录, 对此订单有疑问]
        String money = BillTools.getMoney(list.get(1));

        String shopName = list.get(0);
        String remark = list.get(4);
        String account = "余额";

        BillInfo billInfo = new BillInfo();

        billInfo.setAccountName(account);
        billInfo.setRemark(remark);
        billInfo.setShopRemark(remark);
        billInfo.setShopAccount(shopName);
        billInfo.setMoney(money);
        billInfo.setShopAccount(shopName);


        billInfo.setType(BillInfo.TYPE_INCOME);

        if (billInfo.getAccountName() == null)
            billInfo.setAccountName("支付宝");


        Caches.del(TAG);

        Logs.d("Qianji_Analyze", "捕获的金额:" + money + ",捕获的商户名：" + shopName);

        billInfo.setSource(Alipay.TRANSFER_SUCCESS_ACCOUNT);
        CallAutoActivity.call(context, billInfo);

        return true;
    }


}
