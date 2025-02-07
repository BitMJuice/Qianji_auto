package cn.dreamn.qianji_auto.core.hook;

import java.util.ArrayList;
import java.util.List;

import cn.dreamn.qianji_auto.core.hook.app.alipay.AlipayHook;
import cn.dreamn.qianji_auto.core.hook.app.qianji.QianjiHook;
import cn.dreamn.qianji_auto.core.hook.app.auto.QianjiAuto;
import cn.dreamn.qianji_auto.core.hook.app.wechat.WechatHook;


public class HookList {
    private static HookList mHookList;
    private final List<Class<?>> mListHook = new ArrayList<>();

    public HookList() {
        mListHook.clear();
        mListHook.add(QianjiAuto.class);
        mListHook.add(AlipayHook.class);
        mListHook.add(WechatHook.class);
        mListHook.add(QianjiHook.class);
    }

    public synchronized static HookList getInstance() {
        if (mHookList == null) {
            mHookList = new HookList();
        }
        return mHookList;
    }

    public List<Class<?>> getmListHook() {
        return mListHook;
    }
}
