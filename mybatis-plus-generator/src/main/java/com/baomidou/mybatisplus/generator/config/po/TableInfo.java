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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.jetbrains.annotations.NotNull;


/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @since 2016/8/30
 */
public class TableInfo {

    private final Set<String> importPackages = new HashSet<>();
    private boolean convert;
    private String name;
    private String comment;
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private final List<TableField> fields = new ArrayList<>();
    private boolean havePrimaryKey;
    /**
     * 公共字段
     */
    private final List<TableField> commonFields = new ArrayList<>();
    private String fieldNames;


    /**
     * 构造方法
     *
     * @param name 表名
     * @since 3.5.0
     */
    private TableInfo(@NotNull String name) {
        this.name = name;
    }

    public String getEntityPath() {
        return entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * 逻辑删除
     *
     * @see TableField#isLogicDeleteField()
     * @deprecated 3.5.0
     */
    @Deprecated
    public boolean isLogicDelete(String logicDeletePropertyName) {
        return fields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
    }

    /**
     * 转换filed实体为 xml mapper 中的 base column 字符串信息
     */
    public String getFieldNames() {
        //TODO 感觉这个也啥必要,不打算公开set方法了
        if (StringUtils.isBlank(fieldNames)) {
            this.fieldNames = this.fields.stream().map(TableField::getColumnName).collect(Collectors.joining(", "));
        }
        return this.fieldNames;
    }

    @NotNull
    public Set<String> getImportPackages() {
        return importPackages;
    }

    public boolean isConvert() {
        return convert;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public String getXmlName() {
        return xmlName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public String getControllerName() {
        return controllerName;
    }

    @NotNull
    public List<TableField> getFields() {
        return fields;
    }

    public boolean isHavePrimaryKey() {
        return havePrimaryKey;
    }

    @NotNull
    public List<TableField> getCommonFields() {
        return commonFields;
    }

    /**
     * @author nieqiurong 2021/1/8
     * @since 3.5.0
     */
    public static class Builder {

        private final Entity entity;

        private final TableInfo tableInfo;

        private final GlobalConfig globalConfig;

        private final StrategyConfig strategyConfig;

        public Builder(@NotNull ConfigBuilder configBuilder, @NotNull String name) {
            this.tableInfo = new TableInfo(name);
            this.strategyConfig = configBuilder.getStrategyConfig();
            this.globalConfig = configBuilder.getGlobalConfig();
            this.entity = configBuilder.getStrategyConfig().entity();
            this.tableInfo.name = name;
        }

        public Builder comment(String comment) {
            this.tableInfo.comment = this.globalConfig.isSwagger2()
                && StringUtils.isNotBlank(comment) ? comment.replace("\"", "\\\"") : comment;
            return this;
        }

        public Builder havePrimaryKey(boolean havePrimaryKey) {
            this.tableInfo.havePrimaryKey = havePrimaryKey;
            return this;
        }

        public Builder addField(@NotNull TableField field) {
            if (this.entity.matchSuperEntityColumns(field.getColumnName())) {
                this.tableInfo.commonFields.add(field);
            } else {
                this.tableInfo.fields.add(field);
            }
            return this;
        }

//        public Builder addImportPackages(@NotNull String... pkgs){
//            this.tableInfo.importPackages.addAll(Arrays.asList(pkgs));
//            return this;
//        }

        public TableInfo build() {
            return this.processFileName().convert().importPackage();
        }

        public Builder processFileName() {
            String entityName = entity.getNameConvert().entityNameConvert(this.tableInfo);
            this.tableInfo.entityName = (this.getFileName(entityName, globalConfig.getEntityName(), () -> entity.getConverterFileName().convert(entityName)));
            this.tableInfo.mapperName = this.getFileName(entityName, globalConfig.getMapperName(), () -> strategyConfig.mapper().getConverterMapperFileName().convert(entityName));
            this.tableInfo.xmlName = this.getFileName(entityName, globalConfig.getXmlName(), () -> strategyConfig.mapper().getConverterXmlFileName().convert(entityName));
            this.tableInfo.serviceName = this.getFileName(entityName, globalConfig.getServiceName(), () -> strategyConfig.service().getConverterServiceFileName().convert(entityName));
            this.tableInfo.serviceImplName = this.getFileName(entityName, globalConfig.getServiceImplName(), () -> strategyConfig.service().getConverterServiceImplFileName().convert(entityName));
            this.tableInfo.controllerName = this.getFileName(entityName, globalConfig.getControllerName(), () -> strategyConfig.controller().getConverterFileName().convert(entityName));
            return this;
        }

        private String getFileName(String entityName, String value, Supplier<String> defaultValue) {
            return StringUtils.isNotBlank(value) ? String.format(value, entityName) : defaultValue.get();
        }

        private Builder convert() {
            String tableName = this.tableInfo.name;
            if (this.strategyConfig.startsWithTablePrefix(tableName) || this.entity.isTableFieldAnnotationEnable()) {
                // 包含前缀
                this.tableInfo.convert = true;
            } else if (strategyConfig.isCapitalModeNaming(tableName)) {
                // 包含
                this.tableInfo.convert = !this.tableInfo.entityName.equalsIgnoreCase(tableName);
            } else {
                // 转换字段
                if (NamingStrategy.underline_to_camel == this.entity.getColumnNaming()) {
                    // 包含大写处理
                    if (StringUtils.containsUpperCase(tableName)) {
                        this.tableInfo.convert = true;
                    }
                } else if (!tableInfo.entityName.equalsIgnoreCase(tableName)) {
                    this.tableInfo.convert = true;
                }
            }
            return this;
        }

        private TableInfo importPackage() {
            boolean importSerializable = true;
            String superEntity = this.entity.getSuperClass();
            if (StringUtils.isNotBlank(superEntity)) {
                // 自定义父类
                importSerializable = false;
                this.tableInfo.importPackages.add(superEntity);
            } else {
                if (globalConfig.isActiveRecord() || this.entity.isActiveRecord()) {
                    // 无父类开启 AR 模式
                    this.tableInfo.importPackages.add(Model.class.getCanonicalName());
                    importSerializable = false;
                }
            }
            if (importSerializable) {
                this.tableInfo.importPackages.add(Serializable.class.getCanonicalName());
            }
            if (this.tableInfo.isConvert()) {
                this.tableInfo.importPackages.add(TableName.class.getCanonicalName());
            }
            IdType idType = Optional.ofNullable(this.entity.getIdType()).orElseGet(this.globalConfig::getIdType);
            if (null != idType && this.tableInfo.isHavePrimaryKey()) {
                // 指定需要 IdType 场景
                this.tableInfo.importPackages.add(IdType.class.getCanonicalName());
                this.tableInfo.importPackages.add(TableId.class.getCanonicalName());
            }
            this.tableInfo.fields.forEach(field -> {
                IColumnType columnType = field.getColumnType();
                if (null != columnType && null != columnType.getPkg()) {
                    this.tableInfo.importPackages.add(columnType.getPkg());
                }
                if (field.isKeyFlag()) {
                    // 主键
                    if (field.isConvert() || field.isKeyIdentityFlag()) {
                        this.tableInfo.importPackages.add(TableId.class.getCanonicalName());
                    }
                    // 自增
                    if (field.isKeyIdentityFlag()) {
                        this.tableInfo.importPackages.add(IdType.class.getCanonicalName());
                    }
                } else if (field.isConvert()) {
                    // 普通字段
                    this.tableInfo.importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                }
                if (null != field.getFill()) {
                    // 填充字段
                    this.tableInfo.importPackages.add(com.baomidou.mybatisplus.annotation.TableField.class.getCanonicalName());
                    //TODO 好像default的不用处理也行,这个做优化项目.
                    this.tableInfo.importPackages.add(FieldFill.class.getCanonicalName());
                }
                if (field.isVersionField()) {
                    this.tableInfo.importPackages.add(Version.class.getCanonicalName());
                }
                if (field.isLogicDeleteField()) {
                    this.tableInfo.importPackages.add(TableLogic.class.getCanonicalName());
                }
            });
            return this.tableInfo;
        }
    }
}
