package com.pface.admin.modules.member.enums;

import com.pface.admin.modules.base.enums.IBaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class MediaTypeHandler extends BaseTypeHandler<MediaTypeEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MediaTypeEnum memberTypeEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,memberTypeEnum.getName().toUpperCase());
    }

    @Override
    public MediaTypeEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String v=resultSet.getString(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return MediaTypeEnum.getEnumByName(v);
        }
    }

    @Override
    public MediaTypeEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String v=resultSet.getString(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return MediaTypeEnum.getEnumByName(v);
        }
    }

    @Override
    public MediaTypeEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String v=callableStatement.getString(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return MediaTypeEnum.getEnumByName(v);
        }
    }
}
