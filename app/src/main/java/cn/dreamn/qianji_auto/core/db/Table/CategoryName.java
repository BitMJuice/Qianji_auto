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
public class CategoryName {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;//分类名
    public String icon;//分类图标
    public String level;//分类等级
    public String type;//类型
    public String self_id;//钱迹中自己的id
    public String parent_id;//父类id
    public String book_id;//所属账本
    public String sort;//排序
}

