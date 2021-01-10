package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.H2KeyWordsHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author nieqiurong 2020/10/8.
 */
public class TableFieldTest {

    @Test
    void convertTest() {
        ConfigBuilder configBuilder;
        TableField tableField;
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(),
            TableInfoTest.dataSourceConfig.setKeyWordsHandler(new H2KeyWordsHandler(Collections.singletonList("DESC"))),
            new StrategyConfig.Builder().entityBuilder().naming(NamingStrategy.underline_to_camel).build(), null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "desc").propertyName("desc1").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "DESC").propertyName("desc").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "desc").propertyName("desc").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "desc").propertyName("desc1").build().isConvert());

        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, new StrategyConfig().setEntityTableFieldAnnotationEnable(true), null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "name").propertyName("name").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "name").propertyName("name1").build().isConvert());

        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, new StrategyConfig().setCapitalMode(true), null, new GlobalConfig());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "NAME").propertyName("name").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "USER_NAME").propertyName("userName").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "USER_NAME").propertyName("userName1").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "USER_NAME").propertyName("userName").build().isConvert());

        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, new StrategyConfig().setColumnNaming(NamingStrategy.underline_to_camel), null, new GlobalConfig());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "user_name").propertyName("userName").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "USER_NAME").propertyName("userName").build().isConvert());

        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, new StrategyConfig().setColumnNaming(NamingStrategy.no_change), null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "user_name").propertyName("userName").build().isConvert());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "USER_NAME").propertyName("USER_NAME").build().isConvert());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "NAME").propertyName("name").build().isConvert());

        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, new StrategyConfig().setEntityBooleanColumnRemoveIsPrefix(true), null, new GlobalConfig());
        tableField = new TableField.Builder(configBuilder, "delete").propertyName("delete").build();
        Assertions.assertEquals("delete", tableField.getPropertyName());
        Assertions.assertFalse(tableField.isConvert());
        tableField = new TableField.Builder(configBuilder, "delete").propertyName("delete").build();
        Assertions.assertEquals("delete", tableField.getPropertyName());
        Assertions.assertFalse(tableField.isConvert());
//        tableField = new TableField.Builder(configBuilder, "is_delete").propertyName("isDelete").build();
//        Assertions.assertEquals("delete", tableField.getPropertyName());
//        Assertions.assertTrue(tableField.isConvert());
//        tableField = new TableField.Builder(configBuilder, "is_delete").propertyName("isDelete").build();
//        Assertions.assertEquals("delete", tableField.getPropertyName());
//        Assertions.assertTrue(tableField.isConvert());
    }

    @Test
    void versionFieldTest() {
        ConfigBuilder configBuilder;
        StrategyConfig strategyConfig;
        strategyConfig = new StrategyConfig.Builder().entityBuilder().versionColumnName("c_version").build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "version").propertyName("version").build().isVersionField());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "version").propertyName("version").build().isVersionField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "c_version").propertyName("version").build().isVersionField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "C_VERSION").propertyName("version").build().isVersionField());

        strategyConfig = new StrategyConfig.Builder().entityBuilder().versionPropertyName("version").build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "version").propertyName("version").build().isVersionField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "VERSION").propertyName("version").build().isVersionField());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "c_version").propertyName("cVersion").build().isVersionField());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "C_VERSION").propertyName("cVersion").build().isVersionField());
    }

    @Test
    void logicDeleteFiledTest() {
        ConfigBuilder configBuilder;
        StrategyConfig strategyConfig;
        strategyConfig = new StrategyConfig.Builder().entityBuilder().booleanColumnRemoveIsPrefix(true).logicDeleteColumnName("delete").build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "DELETE").propertyName("delete").build().isLogicDeleteField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "delete").propertyName("delete").build().isLogicDeleteField());

        strategyConfig = new StrategyConfig.Builder().entityBuilder().logicDeletePropertyName("delete").build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "IS_DELETE").propertyName("delete").build().isLogicDeleteField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "is_delete").propertyName("delete").build().isLogicDeleteField());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "is_delete").propertyName("isDelete").build().isLogicDeleteField());

        strategyConfig = new StrategyConfig.Builder().entityBuilder().booleanColumnRemoveIsPrefix(true).logicDeletePropertyName("delete").build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "IS_DELETE").propertyName("delete").build().isLogicDeleteField());
        Assertions.assertTrue(new TableField.Builder(configBuilder, "is_delete").propertyName("delete").build().isLogicDeleteField());
        // TODO 这个不太合理
//        Assertions.assertTrue(new TableField.Builder(configBuilder, "is_delete").propertyName("isDelete").build().isLogicDeleteField());
        Assertions.assertFalse(new TableField.Builder(configBuilder, "is_delete").propertyName("isDelete").build().isLogicDeleteField());
    }

    @Test
    void fillTest() {
        ConfigBuilder configBuilder;
        StrategyConfig strategyConfig;
        strategyConfig = new StrategyConfig.Builder()
            .entityBuilder()
            .addTableFills(
                new TableFill("create_time", FieldFill.INSERT), new TableFill("update_time", FieldFill.UPDATE),
                new Property("createBy", FieldFill.INSERT), new Property("updateBy", FieldFill.INSERT),
                new Column("create_user")
            ).build();
        configBuilder = new ConfigBuilder(new PackageConfig.Builder().build(), TableInfoTest.dataSourceConfig, strategyConfig, null, new GlobalConfig());
        Assertions.assertNotNull(new TableField.Builder(configBuilder, "create_time").build().getFill());
        Assertions.assertNotNull(new TableField.Builder(configBuilder, "update_time").build().getFill());
        Assertions.assertNull(new TableField.Builder(configBuilder, "name").build().getFill());
        Assertions.assertNull(new TableField.Builder(configBuilder, "create_by").build().getFill());
        Assertions.assertNull(new TableField.Builder(configBuilder, "update_by").build().getFill());
        Assertions.assertNotNull(new TableField.Builder(configBuilder, "createBy").propertyName("createBy").build().getFill());
        Assertions.assertNotNull(new TableField.Builder(configBuilder, "updateBy").propertyName("createBy").build().getFill());
        Assertions.assertNotNull(new TableField.Builder(configBuilder, "create_user").propertyName("createUser").build().getFill());
    }
}
