package com.wisedu.scc.love.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Property;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.ReflectBeanUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JZ on 2015/3/11.
 */
public class SqlBuilder {

    public static String PACKAGE = "com.wisedu.scc.love.sqlite.model.";
    public static String[] TABLES = new String[]{
            "User", "ChatRecord", "Login"
    };

    /**
     * 创建所有表语句
     * @return
     */
    public static List<String> allCreateSql(){
        List<String> result = new ArrayList<>();
        // TODO 遍历model文件夹下的所有类名
        String[] tables = TABLES;
        for(String table : tables){
            result.add(geneSql(table));
        }
        return result;
    }

    /**
     * 组装SQL语句
     * @return
     */
    public static String geneSql(String table){
        // 取得Bean的字段
        Map<String, Field> fields = ReflectBeanUtil.getFields(geneClassName(table));

        // 组装创建表语句
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ");
        stringBuffer.append(table);
        stringBuffer.append(" (");
        Set<String> keys = fields.keySet();
        for (String key : keys){
            stringBuffer.append(key);
            stringBuffer.append(" not null,");
        }
        String result = CommonUtil.cutLastChar(stringBuffer.toString())+");";
        return result;
    }

    /**
     * 组装ContentValues
     * @param table
     * @return
     */
    public static ContentValues geneValues(String table, Object obj){
        ContentValues values = new ContentValues();
        Map<String, Field> fields = ReflectBeanUtil.getFields(geneClassName(table));
        for(String key : fields.keySet()){
            Object value = ReflectBeanUtil.getFieldValueByName(obj, key);
            if(null != value)
                values.put(key, value.toString());
        }
        return values;
    }

    /**
     * 组装条件语句
     * @param operator
     * @param strings
     * @return
     */
    public static String geneWhere(String operator, String... strings){
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : strings){
            stringBuffer.append(str);
            stringBuffer.append(" ");
            stringBuffer.append(operator);
            stringBuffer.append(" ? and ");
        }
        stringBuffer.append(" 1=1 ");
        return stringBuffer.toString();
    }

    /**
     * 组装条件语句
     * @param table
     * @return
     */
    public static String geneClassName(String table){
        return PACKAGE.concat(table);
    }

    /**
     * 将游标转换为业务实体
     * @param cursor
     * @param table
     * @return
     */
    public static  <T> T cursor2Entity(Cursor cursor, String table){
        try {
            if(cursor!=null ){
                Class<?> clazz = ReflectBeanUtil.getClazzByBeanName(geneClassName(table));
                T  entity = (T) clazz.newInstance();
                Map<String, Field> fields = ReflectBeanUtil.getFields(geneClassName(table));
                int columnCount = cursor.getColumnCount();
                if(columnCount>0){
                    for(int i=0;i<columnCount;i++){
                        String column = cursor.getColumnName(i);
                        String value = cursor.getString(i);
                        Field field = fields.get(column);
                        Property property = Property.of(clazz, field.getType(), column);
                        property.set(entity, value);
                    }
                    return entity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
