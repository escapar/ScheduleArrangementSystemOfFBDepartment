package cn.edu.shnu.fb.interfaces.assembler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.cglib.core.ReflectUtils;

/**
 * Created by bytenoob on 15/11/10.
 */
public class IOAssembler {

        /**
         * @将POJO对象转成Map
         */

        public static Map toMap(Object obj) {
            Map hashMap = new HashMap();
            try {
                Class c = obj.getClass();
                Method m[] = c.getDeclaredMethods();
                for (int i = 0; i < m.length; i++) {
                    if (m[i].getName().indexOf("get")==0) {
                        //System.out.println("方法名："+m[i].getName());
                        // System.out.println("值："+ m[i].invoke(obj, new Object[0]));
                        hashMap.put(m[i].getName(), m[i].invoke(obj, new Object[0]));
                    }
                }
            } catch (Throwable e) {
                System.err.println(e);
            }
            return hashMap;
        }

    /**
     * 将泛型形参给出的类中设置的属性值转换为Map形式的键值对
     * t一般是pojo类
     *
     * @param params
     * @param t
     */
    public static <T extends Object> void flushParams(Map<String, Object> params,T t) {
        if(params == null || t == null)
            return;

        Class<?> clazz = t.getClass() ;
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields() ;

                for (int j = 0; j < fields.length; j++) { // 遍历所有属性
                    String name = fields[j].getName(); // 获取属性的名字
                    Object value = null;


                        Method method = t.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                        value = method.invoke(t);


                        if(value != null)
                            params.put(name, value);

                }
            } catch (Exception e) {}
        }
    }

    public static <T extends Object> void flushObject(T t, Map<String, Object> params) {
        if(params == null || t == null)
            return;

        Class<?> clazz = t.getClass() ;
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields() ;

                for(int i = 0 ; i< fields.length;i++){
                    String name = fields[i].getName(); // 获取属性的名字


                    Object value = params.get(name);
                    if(value != null && !"".equals(value)){
                        //注意下面这句，不设置true的话，不能修改private类型变量的值
                        fields[i].setAccessible(true);
                        fields[i].set(t, value);
                    }
                }
            }catch(Exception e){}
        }
    }
}
