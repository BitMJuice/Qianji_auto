/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package cn.dreamn.qianji_auto.ui.adapter;


import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.Map;

import cn.dreamn.qianji_auto.R;

/**
 * 主副标题显示适配器
 *
 * @author xuexiang
 * @since 2018/12/19 上午12:19
 */
public class MapAdapter extends SmartRecyclerAdapter<Map<String, String>> {

    public static final String KEY_TITLE = "title";
    public static final String KEY_VALUE = "value";
    public static final String KEY_ID = "id";
    private OnItemClickListener listener;


    public MapAdapter() {
        super(R.layout.map_list);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, Map<String, String> item, int position) {
        SuperTextView map_text = (SuperTextView) holder.findView(R.id.map_text);
        map_text.setLeftString(item.get(KEY_TITLE));
     //   map_text.setCenterString(" -> ");
        map_text.setRightString(item.get(KEY_VALUE));

        map_text.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(Map<String, String> item);
    }
}
