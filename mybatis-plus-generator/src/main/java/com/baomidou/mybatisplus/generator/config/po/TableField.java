/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.IKeyWordsHandler;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 表字段信息
 *
 * @author YangHu
 * @since 2016-12-03
 */
public class TableField {
    private boolean convert;
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String name;
    private String type;
    private String propertyName;
    private IColumnType columnType;
    private String comment;
    private String fill;
    /**
     * 是否关键字
     *
     * @since 3.3.2
     */
    private boolean keyWords;
    /**
     * 数据库字段（关键字含转义符号）
     *
     * @since 3.3.2
     */
    private String columnName;
    /**
     * 自定义查询字段列表
     */
    private Map<String, Object> customMap;

    private final Entity entity;

    /**
     * 构造方法
     *
     * @param configBuilder 配置构建
     * @param name          数据库字段名称
     * @since 3.5.0
     */
    private TableField(@NotNull ConfigBuilder configBuilder, @NotNull String name) {
        this.name = name;
        this.columnName = name;
        this.entity = configBuilder.getStrategyConfig().entity();
    }


    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public String getCapitalName() {
        if (propertyName.length() == 1) {
            return propertyName.toUpperCase();
        }
        if (Character.isLowerCase(propertyName.charAt(1))) {
            return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        }
        return propertyName;
    }

    /**
     * 获取注解字段名称
     *
     * @return 字段
     * @since 3.3.2
     */
    public String getAnnotationColumnName() {
        if (keyWords) {
            if (columnName.startsWith("\"")) {
                return String.format("\\\"%s\\\"", name);
            }
        }
        return columnName;
    }

    /**
     * 是否为乐观锁字段
     *
     * @return 是否为乐观锁字段
     * @since 3.5.0
     */
    public boolean isVersionField() {
        String propertyName = entity.getVersionPropertyName();
        String columnName = entity.getVersionColumnName();
        return StringUtils.isNotBlank(propertyName) && this.propertyName.equals(propertyName)
            || StringUtils.isNotBlank(columnName) && this.name.equalsIgnoreCase(columnName);
    }

    /**
     * 是否为逻辑删除字段
     *
     * @return 是否为逻辑删除字段
     * @since 3.5.0
     */
    public boolean isLogicDeleteField() {
        String propertyName = entity.getLogicDeletePropertyName();
        String columnName = entity.getLogicDeleteColumnName();
        return StringUtils.isNotBlank(propertyName) && this.propertyName.equals(propertyName)
            || StringUtils.isNotBlank(columnName) && this.name.equalsIgnoreCase(columnName);
    }


    public boolean isConvert() {
        return convert;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return keyIdentityFlag;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public IColumnType getColumnType() {
        return columnType;
    }

    public String getComment() {
        return comment;
    }

    public String getFill() {
        return fill;
    }

    public boolean isKeyWords() {
        return keyWords;
    }

    public String getColumnName() {
        return columnName;
    }

    public Map<String, Object> getCustomMap() {
        return customMap;
    }

    public static class Builder {

        private final GlobalConfig globalConfig;

        private final DataSourceConfig dataSourceConfig;

        private final StrategyConfig strategyConfig;

        private final TableField tableField;

        private final Entity entity;

        public Builder(@NotNull ConfigBuilder configBuilder, @NotNull String name) {
            this.globalConfig = configBuilder.getGlobalConfig();
            this.strategyConfig = configBuilder.getStrategyConfig();
            this.dataSourceConfig = configBuilder.getDataSourceConfig();
            this.entity = configBuilder.getStrategyConfig().entity();
            this.tableField = new TableField(configBuilder, name);
            this.tableField.name = name;
            this.tableField.columnName = name;
        }

        public Builder comment(String comment){
            this.tableField.comment =  this.globalConfig.isSwagger2()
                && StringUtils.isNotBlank(comment) ? comment.replace("\"", "\\\"") : comment;
            return this;
        }

        public Builder type(String type) {
            this.tableField.type = type;
            this.tableField.columnType = dataSourceConfig.getTypeConvert().processTypeConvert(this.globalConfig, this.tableField);
            return this;
        }

        public Builder fill(String fill) {
            this.tableField.fill = fill;
            return this;
        }

        public Builder propertyName(@NotNull String propertyName, @NotNull IColumnType columnType) {
            this.tableField.columnType = columnType;
            this.tableField.propertyName = propertyName;
            if (entity.isBooleanColumnRemoveIsPrefix()
                && "boolean".equalsIgnoreCase(columnType.getType()) && propertyName.startsWith("is")) {
                this.tableField.convert = true;
                this.tableField.propertyName = StringUtils.removePrefixAfterPrefixToLower(propertyName, 2);
            }
            return this;
        }

        public Builder columnName(String columnName){
            this.tableField.columnName = columnName;
            return this;
        }

        public Builder primaryKey(boolean isPrimary, boolean autoIncrement){
            this.tableField.keyFlag = isPrimary;
            this.tableField.keyIdentityFlag = autoIncrement;
            return this;
        }

        public Builder customMap(Map<String,Object> customMap){
            this.tableField.customMap = customMap;
            return this;
        }

        public TableField build() {
            if(StringUtils.isBlank(this.tableField.propertyName)){
                this.tableField.propertyName = entity.getNameConvert().propertyNameConvert(this.tableField);
            }
            this.entity.getTableFillList().stream()
                //忽略大写字段问题
                .filter(tf -> tf instanceof Column && tf.getName().equalsIgnoreCase(this.tableField.name)
                    || tf instanceof Property && tf.getName().equals(this.tableField.propertyName))
                .findFirst().ifPresent(tf -> this.tableField.fill = tf.getFieldFill().name());
            return this.keywords().convert();
        }

        private TableField convert() {
            if (entity.isTableFieldAnnotationEnable() || this.tableField.keyWords) {
                this.tableField.convert = true;
                return this.tableField;
            }
            if (entity.isBooleanColumnRemoveIsPrefix()
                && "boolean".equalsIgnoreCase(this.tableField.getPropertyType()) && this.tableField.propertyName.startsWith("is")) {
                this.tableField.convert = true;
            }
            if (strategyConfig.isCapitalModeNaming(this.tableField.name)) {
                this.tableField.convert = !this.tableField.name.equalsIgnoreCase(this.tableField.propertyName);
            } else {
                // 转换字段
                if (NamingStrategy.underline_to_camel == entity.getColumnNaming()) {
                    // 包含大写处理
                    if (StringUtils.containsUpperCase(this.tableField.name)) {
                        this.tableField.convert = true;
                    }
                } else if (!this.tableField.name.equals(this.tableField.propertyName)) {
                    this.tableField.convert = true;
                }
            }
            return this.tableField;
        }

        private Builder keywords(){
            DataSourceConfig dataSourceConfig = this.dataSourceConfig;
            IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
            if (keyWordsHandler != null && keyWordsHandler.isKeyWords(this.tableField.name)) {
                this.tableField.keyWords = true;
                this.tableField.columnName = keyWordsHandler.formatColumn(this.tableField.name);
            }
            return this;
        }
    }
}
