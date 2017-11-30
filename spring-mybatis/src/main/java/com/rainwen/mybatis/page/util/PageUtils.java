package com.rainwen.mybatis.page.util;

import com.rainwen.mybatis.page.Pageable;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.BoundSql;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by rocky on 15/6/27.
 */
public final class PageUtils {

    public static final Param getPageMap(BoundSql boundSql) {
        Object temp = boundSql.getParameterObject();
        if (temp instanceof MapperMethod.ParamMap) {
            PageUtils.Param param = new Param();
            MapperMethod.ParamMap<Object> paramMap = (MapperMethod.ParamMap) temp;
            Iterator<Map.Entry<String, Object>> iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getValue() instanceof Pageable) {
                    param.setEntry(entry);
                    param.setParamMap(paramMap);
                    return param;
                }
            }
        }
        return null;
    }

    public static class Param {

        private MapperMethod.ParamMap<Object> paramMap ;

        private Map.Entry<String, Object> entry;

        public MapperMethod.ParamMap<Object> getParamMap() {
            return paramMap;
        }

        public void setParamMap(MapperMethod.ParamMap<Object> paramMap) {
            this.paramMap = paramMap;
        }

        public Map.Entry<String, Object> getEntry() {
            return entry;
        }

        public void setEntry(Map.Entry<String, Object> entry) {
            this.entry = entry;
        }
    }


}
