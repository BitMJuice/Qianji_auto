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

package cn.dreamn.qianji_auto.core.base.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.dreamn.qianji_auto.core.base.Other;
import cn.dreamn.qianji_auto.core.base.Receive;
import cn.dreamn.qianji_auto.core.base.alipay.Alipay;
import cn.dreamn.qianji_auto.core.base.alipay.BiBiZan;
import cn.dreamn.qianji_auto.core.base.alipay.CardRepayment;
import cn.dreamn.qianji_auto.core.base.alipay.ClientCash;
import cn.dreamn.qianji_auto.core.base.alipay.FundArrival;
import cn.dreamn.qianji_auto.core.base.alipay.MaYi;
import cn.dreamn.qianji_auto.core.base.alipay.PaymentSuccess;
import cn.dreamn.qianji_auto.core.base.alipay.QrCollection;
import cn.dreamn.qianji_auto.core.base.alipay.RedReceived;
import cn.dreamn.qianji_auto.core.base.alipay.Refund;
import cn.dreamn.qianji_auto.core.base.alipay.Repayment;
import cn.dreamn.qianji_auto.core.base.alipay.TransferIntoYuebao;
import cn.dreamn.qianji_auto.core.base.alipay.TransferReceived;
import cn.dreamn.qianji_auto.core.base.alipay.TransferSucceed;
import cn.dreamn.qianji_auto.core.base.alipay.TransferSucceedAccount;
import cn.dreamn.qianji_auto.core.base.alipay.YuEBao;
import cn.dreamn.qianji_auto.core.base.alipay.YuLiBao;
import cn.dreamn.qianji_auto.core.base.wechat.Payment;
import cn.dreamn.qianji_auto.core.base.wechat.Wechat;
import cn.dreamn.qianji_auto.core.base.wechat.WechatIncomeMoney;
import cn.dreamn.qianji_auto.core.base.wechat.WechatIncomeShop;
import cn.dreamn.qianji_auto.core.base.wechat.WechatPaymentRefund;
import cn.dreamn.qianji_auto.core.base.wechat.WechatPaymentTransfer;
import cn.dreamn.qianji_auto.core.base.wechat.WechatQrCollection;
import cn.dreamn.qianji_auto.core.base.wechat.WechatRedPackage;
import cn.dreamn.qianji_auto.core.base.wechat.WechatRedPackageReceived;
import cn.dreamn.qianji_auto.core.base.wechat.WechatRedRefund;
import cn.dreamn.qianji_auto.core.base.wechat.WechatTransferRefund;
import cn.dreamn.qianji_auto.core.helper.AutoBillService;
import cn.dreamn.qianji_auto.core.utils.BillInfo;
import cn.dreamn.qianji_auto.core.utils.CallAutoActivity;
import cn.dreamn.qianji_auto.utils.tools.Logs;

public class XposedBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        if (action.equals("cn.dreamn.qianji_auto.XPOSED_LOG")) {
            Bundle extData = intent.getExtras();
            if (extData == null) return;
            String tag = extData.getString("tag");
            String msg = extData.getString("msg");
            Logs.i(tag, msg);
        } else if (action.equals("cn.dreamn.qianji_auto.XPOSED")) {


            Bundle extData = intent.getExtras();
            if (extData == null) return;
            String type = extData.getString("type");
            String from = extData.getString("from");
            String data = extData.getString("data");
            String title = extData.getString("title");
            if (title == null) title = "";
            if (from == null || data == null || type == null) return;
            String str =

                    "源APP: " + type + "\n" +
                            "源自类型: " + from + "\n" +
                            "源标题: " + title + "\n" +
                            "数据: " + data + "\n";
            Logs.i(str);
            BillInfo billInfo = null;
            switch (type) {
                case Receive.ALIPAY:
                    switch (from) {
                        case Alipay.BIBIZAN:
                            //笔笔攒消息
                            billInfo = BiBiZan.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.REPAYMENT:

                            billInfo = Repayment.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.CARD_REPAYMENT:
                            billInfo = CardRepayment.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.PAYMENT_ORDING:
                        case Alipay.PAYMENT_SUCCESS:
                            billInfo = PaymentSuccess.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.QR_COLLECTION:
                            billInfo = QrCollection.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.REC_YUEBAO:
                            billInfo = YuEBao.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.REC_YULIBAO:
                            billInfo = YuLiBao.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.RED_RECEIVED:
                            billInfo = RedReceived.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.REFUND:
                            billInfo = Refund.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.FUNDS_ARRIVAL:
                            billInfo = FundArrival.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.TRANSFER_RECEIVED:
                            billInfo = TransferReceived.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.TRANSFER_SUCCESS:
                            billInfo = TransferSucceed.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.TRANSFER_SUCCESS_ACCOUNT:
                            billInfo = TransferSucceedAccount.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.MAYI:
                            billInfo = MaYi.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.CLIENT_CASH:
                            billInfo = ClientCash.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.TRANSFER_YUEBAO:
                        case Alipay.TRANSFER_INTO_YUEBAO:
                            billInfo = TransferIntoYuebao.getInstance().tryAnalyze(data, from);
                            break;
                        case Alipay.CANT_UNDERSTAND:
                            String body = Other.getTextWithAlipay(data);
                            Logs.i("未识别信息文本", body);
                            billInfo = Other.regular(title, body, context);
                            break;
                        default:
                            break;
                    }
                    if (billInfo != null && billInfo.getSource() == null) {
                        billInfo.setSource("支付宝");
                    }
                    break;
                case Receive.WECHAT:
                    switch (from) {
                        case Wechat.PAYMENT:
                            billInfo = Payment.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.RECEIVED_QR:
                            billInfo = WechatQrCollection.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.PAYMENT_TRANSFER:
                            billInfo = WechatPaymentTransfer.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.PAYMENT_TRANSFER_RECEIVED:
                            //  WechatTransferReceived.getInstance().tryAnalyze(data,context);
                            // 和上面那个一样
                            break;
                        case Wechat.RED_PACKAGE:
                            billInfo = WechatRedPackage.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.RED_PACKAGE_RECEIVED:
                            billInfo = WechatRedPackageReceived.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.PAYMENT_TRANSFER_REFUND:
                            billInfo = WechatTransferRefund.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.PAYMENT_REFUND:
                            billInfo = WechatPaymentRefund.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.RED_REFUND:
                            billInfo = WechatRedRefund.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.INCOME_SHOP:
                            billInfo = WechatIncomeShop.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.MONEY_INCOME:
                            billInfo = WechatIncomeMoney.getInstance().tryAnalyze(data, from);
                            break;
                        case Wechat.CANT_UNDERSTAND:
                            String body = Other.getTextWithWechat(data);
                            Logs.i("未识别信息文本", body);
                            billInfo = Other.regular(title, body, context);
                            break;
                        default:
                            break;
                    }
                    if (billInfo != null && billInfo.getSource() == null) {
                        billInfo.setSource("微信");
                        if (billInfo.getAccountName() == null) {
                            billInfo.setAccountName("微信");
                        }
                    }
                    break;
                default:
                    break;
            }
            // str += ">>>>>>>>>>>>>>>>>>账单分析完毕 \n";
            if (billInfo != null) {

                CallAutoActivity.call(context, billInfo);
            } else {
                Logs.i(">>>账单无效");
            }


        }
    }
}
