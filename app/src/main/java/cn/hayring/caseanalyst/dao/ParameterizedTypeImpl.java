package cn.hayring.caseanalyst.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Hayring
 * @date 2021/8/25
 * @description 泛型类辅助解析
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}